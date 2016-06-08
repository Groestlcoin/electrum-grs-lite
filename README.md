Multicoin Wallet
===============

This project is attempt to port [Coinomi wallet](https://coinomi.com) to desktop.

This application using Java and compatible for all desktop platform.

## Building the app

Make sure that you have JDK 8 installed before building. You can use [ant](http://ant.apache.org/bindownload.cgi) to build jar:

- To build Linux version run command 'ant deb'. (Available on MacOs or Linux platform) Obtained multicoin.deb can be found in out/production/multicoin/wallet-deb/
- To biuld MacOs version: run command 'ant mac' (Available on MacOs platform). Obtained multicoin.dmg can be found in out/production/multicoin/wallet-mac/ 
- To biuld Windows version: run command 'ant win' (Available on MacOs or Windows platform). Obtained multicoin.exe can be found in out/production/multicoin/wallet-win/  

## Installing app and dependencies

- Application ceated for MacOs platform contains embedded java runtime and have'nt any dependencies. You need only run multicoin.dmg and drag'n'drop Multicoin app to Applications folder.
- you need install [Java 8](http://java.com/) to run application on Linux or Windows platform. If you using openJDK on Linux you must install openjfx. When you will install app from deb package all dependencies will be installed automatically.  

## Run app

- On Windows you can run multicon.exe or create desktop link to run app manually
- On Linux you can run 'multicoin' command in terminal or copy /usr/share/applications/multicoin.desktop launcher to ~/Desktop folder and use it to run app
- On MacOs you can use the same way to run like other applications

When application will run first time, file "wallet.dat" will be created in current directory (User's home directory for Linux or MacOs platform). Don't loose this file. 
Also you must store mnemonic code what will be generated at first launch. 
With mnemonic code you can restore your wallet when you lost wallet.dat file or forgot wallet password

## Automatic update

When a new version of the application is available, the app will automatically update themselves and notify the user. 
After upgrade the application will be closed and will need to re-open. This option available now for MacOs and Linux platforms.

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
