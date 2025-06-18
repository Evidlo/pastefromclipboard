package com.github.evidlo.pastefromclipboard

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PasteActivity : Activity() {
    
    companion object {
        private const val TAG = "PasteActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Log.d(TAG, "PasteActivity created with intent: ${intent?.action}")
        
        // Set the layout to make the activity visible
        setContentView(R.layout.activity_paste)
        
        // Add a small delay to ensure the activity is in the foreground
        Handler(Looper.getMainLooper()).postDelayed({
            handleGetContentIntent()
        }, 100) // 100ms delay
    }
    
    private fun handleGetContentIntent() {
        try {
            Log.d(TAG, "Attempting to access clipboard...")
            
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            
            // Check if we have clipboard access
            if (!clipboardManager.hasPrimaryClip()) {
                Log.w(TAG, "No clipboard data available - hasPrimaryClip() returned false")
                setResult(Activity.RESULT_CANCELED)
                finish()
                return
            }
            
            val clipData = clipboardManager.primaryClip
            if (clipData == null || clipData.itemCount == 0) {
                Log.w(TAG, "Clipboard data is null or empty - itemCount: ${clipData?.itemCount}")
                setResult(Activity.RESULT_CANCELED)
                finish()
                return
            }
            
            Log.d(TAG, "Clipboard has ${clipData.itemCount} items")
            
            // Get the first item from clipboard
            val clipItem = clipData.getItemAt(0)
            Log.d(TAG, "Clipboard item: text=${clipItem.text}, uri=${clipItem.uri}, htmlText=${clipItem.htmlText}")
            
            // Try to get URI from clipboard
            val uri = clipItem.uri
            if (uri != null) {
                Log.d(TAG, "Found URI in clipboard: $uri")
                returnClipboardUri(uri)
                return
            }
            
            // Try to get text that might be a file path
            val text = clipItem.text?.toString()
            if (!text.isNullOrEmpty()) {
                Log.d(TAG, "Found text in clipboard: $text")
                val textUri = tryCreateUriFromText(text)
                if (textUri != null) {
                    returnClipboardUri(textUri)
                    return
                }
            }
            
            // Try to get HTML text (might contain image data)
            val htmlText = clipItem.htmlText?.toString()
            if (!htmlText.isNullOrEmpty()) {
                Log.d(TAG, "Found HTML text in clipboard: $htmlText")
                // Could potentially extract image data from HTML
            }
            
            // If we get here, we couldn't extract a valid file URI
            Log.w(TAG, "Could not extract valid file URI from clipboard")
            setResult(Activity.RESULT_CANCELED)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error handling clipboard data", e)
            setResult(Activity.RESULT_CANCELED)
        } finally {
            finish()
        }
    }
    
    private fun tryCreateUriFromText(text: String): Uri? {
        return try {
            // Try to parse as URI
            val uri = Uri.parse(text)
            Log.d(TAG, "Successfully parsed text as URI: $uri")
            uri
        } catch (e: Exception) {
            Log.d(TAG, "Text is not a valid URI: $text", e)
            null
        }
    }
    
    private fun returnClipboardUri(uri: Uri) {
        try {
            // Create result intent with the clipboard URI
            val resultIntent = Intent().apply {
                data = uri
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            Log.d(TAG, "Returning clipboard URI: $uri")
            setResult(Activity.RESULT_OK, resultIntent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error returning clipboard URI", e)
            setResult(Activity.RESULT_CANCELED)
        }
    }
} 