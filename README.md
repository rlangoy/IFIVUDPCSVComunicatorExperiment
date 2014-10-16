UDP CSV Comunicator Experiment
===============================
###Warning this is a project under develoment and it might not work :) !

The Application sends A UDP packages that contains a CSV(Coma Separeted Values) ASCII string.   
###![alt tag](https://raw.githubusercontent.com/rlangoy/IFIVUDPCSVComunicatorExperiment/AlphaDev/doc/Images/MainActivity.PNG)

#Working functionalities:
![alt tag](https://raw.githubusercontent.com/rlangoy/IFIVUDPCSVComunicatorExperiment/AlphaDev/doc/Images/IPAddressDialog.PNG)
###Dialog to Configure IP Address and Portnumber
  
![alt tag](https://raw.githubusercontent.com/rlangoy/IFIVUDPCSVComunicatorExperiment/AlphaDev/doc/Images/PhoneIpAddress.PNG)
###Dialog to Show the Wifi IP-Address
  
![alt tag](https://raw.githubusercontent.com/rlangoy/IFIVUDPCSVComunicatorExperiment/AlphaDev/doc/Images/SendButton.PNG)
###Sends the UDP message displayed in the EditText field when the user pushes the button "Send CVS String"
  
Work in the develoment braches/Intentions
--------------------------
The Application is intended to used in the cource EN-SOC3000 (System on chip design) to show how to interact
 width a Android device using:  
     1. The UDP protocol for sending and recieveing ASCII strings  
     2. How to parse(decode) a CVS (Coma separated Value) string.  
       
       
Compilation / Build Instructions
---------------------------------

* Download Android Studio.
* Download ActionBarSherlock  from http://actionbarsherlock.com/download.html
* Extract the downloaded files.
  Fom the Menu in  Android Studio click "File" | "Import Module" and specify the location of the folder "actionbarsherlock"
   (From the extracted files ie:C:\Android\ActionSherloc\JakeWharton-ActionBarSherlock-5a15d92\actionbarsherlock)



Developed By
------------
* Rune Langøy

License
-------

    Copyright 2014 Rune Langøy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.