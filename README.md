# Account Service

The Account Service is a microservice in the DTUPay system responsible for managing user accounts.

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
├── token-service 
└── system-test
```

### Links to the repositories
* [Account Service](https://github.com/duerko2/account-service)
* [Bank Service](https://github.com/duerko2/bank-service)
* [Payment Service](https://github.com/duerko2/payment-service)
* [Reporting Service](https://github.com/duerko2/reporting-service)
* [Token Service](https://github.com/duerko2/token-service)
* [System Test](https://github.com/duerko2/system-test)


### Troubleshooting
If you cannot run the build.sh script, ensure that the script is runnable by running the following command (assuming you are in the account-service directory)
```Bash
chmod +x build.sh
```
