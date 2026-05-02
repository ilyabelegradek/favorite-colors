# This app is a work in progress.

# Privacy Policy for Favorite Colors

**Last Updated:** 5/2/2026

Favorite Colors is a Jetpack Compose Kotlin app developed by Ilya Belegradek.
Read on to learn how this app collects and uses personal data.

### 1. Data Collection and Usage

To provide the app's functionality (voting for colors), the following services are used:

* **Firebase Authentication:** Google Sign-In is used to authenticate users. This allows the app to
  verify
  your identity and ensure each user only votes once. Access to your Google User ID and
  email address provided by the authentication process is stored.
* **Firebase Realtime Database:** Your color vote is stored. This vote is associated with your
  unique
  User ID to prevent duplicate voting.

### 2. Data Visibility

Other users can see the aggregate number of votes for each color. Individual votes are anonymous to
other users; they cannot see which color you voted for or any of your personal information.

### 3. Third-Party Service Providers

**Google Firebase** powers the app's backend. Google may collect certain information
automatically, such as IP addresses, device identifiers, and usage diagnostics, as described in
the [Google Privacy Policy](https://policies.google.com/privacy).

[Skydoves' Color Picker](https://github.com/skydoves/colorpicker-compose) allows users to add a
new color other users can vote for. This library does not collect any user data.

### 4. Data Deletion

Users have the right to request the deletion of their account and associated voting data. To request
data deletion, please perform the following steps:

1) Open the Favorite Colors app
2) Sign in (if you're not automatically signed in)
3) Tap the "My Account" button
4) Tap "Delete My Account"
5) In the confirmation dialog, tap "Yes, delete my account"

Your Google account will be removed from our database and your vote for your favorite color will be
cleared. .

### 5. Contact

If you have any questions or suggestions about the Privacy Policy, do not hesitate to contact me at:
ilyabelegradek25@gmail.com