# cloudcard-photo-downloader

This project automatically downloads photos from [CloudCard Online Photo Submission](http://onlinephotosubmission.com/).

## Requirements

- Java 1.8 (to run from the Jar file)
- Java 1.6+ (to build from Source)
- Office level access to [CloudCard Online Photo Submission](http://onlinephotosubmission.com/)

## Usage

1. Download the [jar file](https://github.com/online-photo-submission/cloudcard-photo-downloader/raw/master/cloudcard-photo-downloader-2018.05.22.1624.jar).
1. Download [application.properties](https://raw.githubusercontent.com/online-photo-submission/cloudcard-photo-downloader/master/src/main/resources/application.properties) into the same directory
1. Configure `application.properties`
1. Run `java -jar cloudcard-photo-downloader.jar`

## Configuration

`application.properties` is the main configuration file and should be saved in the same directory as the downloader.  You configure the settings using environment variables, JVM variables, etc.  See the [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) for more information.

Below are descriptions of each option:

- cloudcard.api.url  
  - default: `https://api.onlinephotosubmission.com/api`
  - description: This option allows you to specify the URL of your CloudCard Online Photo Submission API.  Most users will not need to change this setting.  Generally, this is only useful if you are testing the integration using the test intance `https://test-api.onlinephotosubmission.com/api`.
- cloudcard.api.accessToken
  - default: none
  - description: this setting holds the API access token for your service account and must be set before the exporter to run. On a Unix/Linux based operating system, you can use `get-token.sh` to get your access token.
- downloader.delay.milliseconds
  - default: `600000` (Ten Minutes)
  - this is the amount of time the exporter will wait between exports
- downloader.photoDirectory
  - default: none
  - description: this is the absolute path to the directory into which the photos will be saved
- downloader.udfDirectory
  - default: 
- downloader.udfFilePrefix
  - default: `CloudCard_Photos_`
  - description: The first part of the UDF filename.  The UDF filename is constructed by concatonating the `udfFilePrefix`, a date formated accordning to `batchIdDateFormat`, and `udfFileExtension`.  Given the defaults the generated filename will look something like `CloudCard_Photos_201805221648.udf`
- downloader.udfFileExtension
  - default: `.udf`
  - description: The extension to use for the UDF filename (see the description for `downloader.udfFilePrefix`)
- downloader.descriptionDateFormat
  - default: `MMM dd 'at' HHmm`
  - description: The format of the date in the UDF description (i.e. `!Description: Photo Import May 22 at 1541`)
- downloader.batchIdDateFormat
  - default: `YYYYMMddHHmm`
  - description: The format of the date in the UDF batch ID field (i.e. `!BatchID: 201805221541`) and in the filename suffix (see the description for `downloader.udfFilePrefix`) 
- downloader.createdDateFormat
  - default: `YYYY-MM-dd`
  - description: The format of the date in the UDF Created field (i.e. `!Created: 2018-05-22`)
- downloader.enableUdf
  - default: `true`
  - description: Enable/Disable UDF file generation
## Support and Warranty
THIS PROJECT IS DISTRIBUTED WITH NO WARRANTY.  SEE THE LICENSE FOR FULL DETAILS.
If your organization needs fully warranteed CloudCard integration software, consider Cloud Photo Connect from [Vision Database Systems](http://www.visiondatabase.com/).
