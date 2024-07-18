# 366pi-reqrez App

366pi is a sample Android application that demonstrates fetching and displaying user data using Retrofit and Jetpack Compose.

## Features

- Fetches user data from an API using Retrofit.
- Displays a list of users with their avatars, names, and email addresses.
- Allows adding new users with validation for empty fields.
- Uses Jetpack Compose for UI development.
- Implements error handling for network failures.

## Screenshots
![Homepage](/sample/Screenshot_2024-07-11-22-10-40-270_com.example.a366pi.jpg)

![Add User](/sample/Screenshot_2024-07-11-22-10-45-845_com.example.a366pi.jpg)

![Successfull PUSH](/sample/Screenshot_2024-07-11-22-11-00-051_com.example.a366pi.jpg)

![Recalling the userdata GET](/sample/Screenshot_2024-07-11-22-11-04-533_com.example.a366pi.jpg)

![Unsuccessfull PUSH](/sample/Screenshot_2024-07-11-22-11-34-783_com.example.a366pi.jpg)

![No Internet](/sample/Screenshot_2024-07-11-22-11-14-666_com.example.a366pi.jpg)

## Setup

To run this project:

1. Clone the repository:

```shell
https://github.com/mishrabiswajit/366pi-reqrez.git
```

2. Open the project using Android Studio.

3. Build and run the project on an emulator or a physical device.

## Dependencies

- Retrofit: For making network requests.
- Coil: For image loading and caching.
- Jetpack Compose: For building the UI declaratively.

## Usage

- Upon launching the app, it checks for an internet connection and fetches the list of users.
- Users are displayed in a list with their avatars, names, and email addresses.
- To add a new user, enter the employee's name and post, and click on the "Add User" button.

## Contributing

Contributions are welcome! Here's how you can contribute:
- Fork the repository.
- Create a new branch (`git checkout -b feature/new-feature`).
- Make your changes.
- Commit your changes (`git commit -am 'Add new feature'`).
- Push to the branch (`git push origin feature/new-feature`).
- Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Icons used in the app are from [Material Design Icons](https://material.io/resources/icons/).
- Emoji artwork used in the app is from [EmojiOne](https://www.emojione.com/).
