# Calendar

# Google Calendar Clone

This is a Google Calendar clone built using Kotlin Jetpack Compose. It includes authentication with
Firebase, utilizes a REST API, implements dependency injection with Dagger Hilt, and manages data
storage with Room Database.

## Getting Started

### Clone the Repository:

`git clone https://github.com/Victor-Agbo/Calendar.git`

### Open the Project:

1. Open Android Studio.
2. Select "Open an existing Android Studio project."
3. Navigate to the cloned repository and select the project directory.

### Configure Firebase:

1. Create a Firebase project at Firebase Console.
2. Add an Android app to your Firebase project (com.victor.calendar) and follow the setup
   instructions to download the google-services.json file.
3. Place the google-services.json file in the app/ directory of your project.

### Configure REST API:

- Update the API endpoints and keys in the project's API configuration file

### Build and Run:

1. Connect your Android device or launch an Android emulator.
2. Click on the "Run" button in Android Studio to build and run the app on your device/emulator.
3. Sign in and Use the App:
4. Upon launching the app, users can sign in or create a new account using Firebase authentication.
5. Explore the app's features such as event creation, editing, and deletion.

Usage

- Authentication: Users can sign in or create a new account using Firebase authentication.
- Event Management: Create, edit, and delete events. Changes are synced with the backend API and
  stored locally in the Room Database.

## Contributing

Contributions to this project are welcome! If you have suggestions, bug fixes, or feature
enhancements, please open an issue or submit a pull request.


![](https://media.licdn.com/dms/image/D4D22AQGl7UJGnvtqyA/feedshare-shrink_800/0/1710528271539?e=1714608000&v=beta&t=CTWKrYH--2z9DI-dBCqGvYsR2kNFyNyrxKAfXImtszo)
