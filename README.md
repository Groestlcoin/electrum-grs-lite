Multicoin Wallet
===============

I like Coinomi wallet (https://coinomi.com) but this application exists only for mobile devices.
This project is attempt to port my favorite cryptocurrency wallet to desktop.

This application using Java and compatible for all desktop platform.

## Building the app

Make sure that you have JDK 7 installed before building. You can use ant to build jar.

## Run app

java -jar multicoin.jar

When application will run first time, file "wallet.dat" will be created in current directory. Don't loose this file. 
Also you must store mnemonic code what will be generated at first launch. 
With mnemonic code you can restore your wallet when you lost wallet.dat file or forgot wallet password

## Contributions

Your contributions are very welcome, be it translations, extra features or new coins support. Just
fork this repo and create a pull request with your changes.

## Features

With the Multicoin Wallet you can store, send and receive multiple types of cryptocurrency using only one wallet application.

 - HD enabled - manage multiple accounts and never reuse addresses ([Bip32](https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki)/[Bip44](https://github.com/bitcoin/bips/blob/master/bip-0044.mediawiki) compatible)
 - Masterseed based - make one backup for multiple coins in your wallet and be safe for ever. ([Bip39](https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki))
 - 100% control over your private keys, they never leave your device unless you export them
 - No block chain download - install and run in seconds
 - Secure your wallet with a password
 - Compatible with other bitcoin services through the `bitcoin:` URI scheme
 
Please note that cryptocurrency is still experimental and this app comes with no warranty - I can not exclude that the software contains bugs. Please make sure you have backups of your wallet.dat file and seed. 
Do not use this for more than you are willing to lose.
