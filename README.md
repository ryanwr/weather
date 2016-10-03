# Weather

Weather, a simple Material Design weather app with configurable providers.
The project is still under development, if you find a bug or have a
suggestion [open an issue](https://github.com/ryanwr/weather/issues/new)
or [contact me](https://github.com/ryanwr).

Coming soon to Google Play.

## Getting started

> How can I help?

View the [open issues here](https://github.com/ryanwr/weather/issues),
and read the [contributing](https://github.com/ryanwr/weather/blob/master/CONTRIBUTING.md) information.

> About the app

The app is based on MVP and clean architecture, there are three main packages: data, domain, ui.

  - Data: Responsible for interaction with the network and database
  - Domain: Contains Models for data and any logic for interaction with data layer
  - UI: Views, Presenters and Adapters in a package-by-feature structure

Libraries used: RxJava, Retrofit, ButterKnife, StorIO, Dagger 2, Android Support Library

## Setup Environment

1. Create a developer account for apixu.com, darksky.net and openweathermap.org
2. Set the following in your local.properties file:
  - `ApixuApiToken`=your_apixu_api_token
  - `OpenWeatherApiToken`=your_openweathermap_api_token
  - `DarkSkyApiToken`=your_darksky_api_token

Note: It is not necessary to have a valid token to run the app however the
variables must be present in the local.properties file for the app to compile

## Changelog

## License

