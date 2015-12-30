# marathon-client [![Build Status](https://travis-ci.org/vivekkothari/marathon-client.svg?branch=master)](https://travis-ci.org/vivekkothari/marathon-client)

This project is a Java library for communicating with Marathon API. At this point this library supports version v2 of Marathon API, please refer [docs](https://mesosphere.github.io/marathon/docs/rest-api.html) for more details.

## Using marathon-client in your maven project
[![Clojars Project](http://clojars.org/com.github.vivekkothari/marathon-client/latest-version.svg)]

Please look at [releases](https://github.com/mohitsoni/marathon-client/releases) page for more versions.

## Usage

### Initialization

The following piece of code initializes the client. ```MarathonClient.getInstance()``` method expects the endpoint for marathon:

```
String endpoint = "<Marathon's endpoint>";
Marathon marathon = MarathonClient.getInstance(endpoint);
```

### Getting all applications

The following will return all the applications that have been created:

```
GetAppsResponse appsResponse = marathon.getApps();
```

### Create a new application

The following example demonstrates how a new application can be created:
```
App app = new App();
app.setId("echohisleepbye-app");
app.setCmd("echo hi; sleep 10; echo bye;");
app.setCpus(1.0);
app.setMem(16.0);
app.setInstances(1);
marathon.createApp(app);
```

### Get details about an existing application

The following example, demostrates how to get details about an already created application:

```
GetAppResponse appGet = marathon.getApp("echohisleepbye-app");
```

### Delete an application

The following example demostrate, how one can delete an existing application:
```
marathon.deleteApp("echohisleepbye-app");
```

## Building

This project is built using [Apache Maven](http://maven.apache.org/).

Run the following command from the root of repository, to build the client JAR:

```
$ mvn clean install
```

## Bugs

Bugs can be reported using Github issues.
=======
marathon-client
>>>>>>> f12c5d99d04e051a05587631fb3744e5a2675ad5