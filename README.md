# Account-Service

The Account Service is a microservice in the DTUPay system responsible for managin user accounts.

## Requirements
To run the service locally, ensure that you have the proper dependencies installed
* Maven
* Docker Compose

## Build
To download, build and test the microservice, run the following commands

```Bash
git clone https://github.com/duerko2/account-service
```
```Bash
cd account-service
```
```Bash
./build.sh
```

## Notes
To run the microservice together with the other services in DTUPay, it is required that you follow the directory schema as followed

```Bash
DTUPay
├── account-service 
├── bank-service
├── payment-service
├── reporting-service
├── system-test
├── token-service 
└── local_run_and_test.sh
```

### Links to the repositories
* [Account Service](https://github.com/duerko2/account-service)
* [Bank Service](https://github.com/duerko2/account-service)
* [Payment Service](https://github.com/duerko2/account-service)
* [Reporting Service](https://github.com/duerko2/account-service)
* [Token Service](https://github.com/duerko2/account-service)
* [System Test](https://github.com/duerko2/account-service)


### Troubleshooting
If you cannot run the build.sh script, ensure that the script is runnable by running the following command
```Bash
chmod +x build.sh
```
