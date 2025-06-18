# Paste - Android Clipboard Content Provider

<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" stroke="black" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-clipboard">
  <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2" />
  <rect x="9" y="2" width="6" height="4" rx="1" ry="1" />
</svg>

A simple Android application that acts as a content provider for clipboard data. When you select this app from an ACTION_GET_CONTENT intent (like when inserting images in other apps), it will return the latest file URI from your clipboard.

An Android application which provides simple clipboard support to applications which support inserting files from other apps (e.g. file picker).

**CAVEAT: this project was almost entirely LLM-generated** (see [chat.md](chat.md))

## Use Case

This app is particularly useful for images. When you copy an image in Firefox (or any other app), you can use this app to "paste" it into applications that support inserting images from file but not from clipboard, without having to save the image first.

## How it Works

1. The app advertises itself as handling `ACTION_GET_CONTENT` intents
2. When selected from the app picker, it appears as "Paste"
3. It reads the current clipboard content
4. If the clipboard contains a file URI, it returns that URI to the calling app
5. The calling app can then use the URI to access the file

## Installation

1. Build the APK using Android Studio or Gradle
2. Install the APK on your Android device
3. Grant clipboard permissions when prompted

## Usage

1. Copy an image or file to your clipboard (e.g., from Firefox)
2. In the target app, when prompted to select a file/image, choose "Paste" from the app picker
3. The clipboard content will be automatically selected and returned to the calling app

## Technical Details

- **Package**: `com.github.evidlo.pastefromclipboard`
- **Minimum SDK**: 28 (Android 9.0)
- **Target SDK**: 34 (Android 14)
- **Permissions**: `READ_CLIPBOARD_IN_BACKGROUND`

## Building

```bash
./gradlew assembleDebug
```

The APK will be generated in `app/build/outputs/apk/debug/`.

## Limitations

- Only works with file URIs in the clipboard
- Requires clipboard permissions
- May not work with all clipboard content types
- Some apps may not properly handle the returned URI

## License

This project is open source and available under the MIT License. 