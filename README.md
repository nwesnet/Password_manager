# Password manager 

Password manager. A personal desktop application to securely store and manage your passwords, cards, account credentials, links, and wallets. Your data is encrypted with your personal key and store locally - with optional synchronization to a server for secure backup, and manage from anywhere and any platform.

# Feature

Local and server storage: choose to keep data on your device or sync with a server
All sensitive information is encrypted using your private key
Password vault: store all your accounts in one secure place
Card and wallet Storage: Save card details and crypt wallets.
Link managment: You can store the URLs 
Password generator: Generate strong, random passowrd

# Build with 

java - Core application language 
javafx - UI framework 
SQLite3 - Local data storage
JSON - Data serialization 
CSS - UI styling

# HTTPS Access and Certificate Instuctions
To use the application with secure HTTPS access, a custom certificate is used. If you want to use the sync functions you should to add the mywebsite.crt file into your $JAVA_HOME/lib/security/cacerts file with using this command:

sudo keytool -importcert -trustcacerts -file mywebsite.crt -alias mywebsite -keystore $JAVA_HOME/lib/security/cacerts

you should to be in the same directory where you have mywebsite.crt The password is: changeit


# Installation

https://github.com/nwesnet/Password_manager.git

It is open source project that you can just download from git.
Also you can visite my website with using this ip:
https://[2a01:4f9:c013:c9bb::1]:8443/
and find this page
projects/passwordmanager


You will get the zip archive with javafx sdk, run script for your OS, jar file, CA Certificate, and this instruction.
To run this app you should to have java 21+ on your computer.


  
