# TrojanCheckIn-Group29

## Developer Instructions

- Download <a href="https://developer.android.com/studio/?gclid=Cj0KCQiAnKeCBhDPARIsAFDTLTLScJATfFQhJU4dNikkfslmc_vpWlRZhtPw5z2m3grUkGY1-KqTvEMaAkuKEALw_wcB&gclsrc=aw.ds"> Android Studio </a>, preferably the latest version
- When Android Studio opens, select "Get from Version Control"
- Enter "https://github.com/CSCI310-Group29/TrojanCheckIn-Group29" for the repository url
- You should have now cloned the github repository

- If opening project from project folder, Open project folder within Android Studio


## Running the Project

Uncompress the assignment zip file, and then uncompress the project zip file. In android studio, import the project folder by "open an existing project" in Android Studio.

Once you run the project, Android Studio should have automatically changed the path to your Android SDK in the <root>/local.properties file. Otherwise, please specify the location to the Android SDK manually in this file.


#### Set up Android Emulator
You should have the included Android emulator. Ensure that the webcam is enabled in order to scan QR codes.
- In Android Studio, go to the "Tools" tab
- Select "AVD Manager"
- If no Virtual Devices exist, select "Create Virtual Device..." at the bottom
    - Select Pixel 3a XL for device type
    - Select Android Oreo 8.0 / API 26 
    - Follow the rest of the instructions and Finish
- For the Pixel 3a XL API 26 VD, open the "Edit" action by clicking on the pencil icon
- Click "Show Advanced Settings" 
- Set "Camera: Back:" to "Webcam0"
- Finish 
- Open Actions dropdown and Wipe Data
- Open Actions dropdown and Cold Boot

#### Run the App
Open "Run" tab and select Run 'app' OR click the green "Run 'app'" arrow in the top right.   
App should build and open in the Android emulator shortly.    

Database will be initialized with buildings and capacities.  
Note: May need to take a picture of the QR code to scan with computer webcam and sign in
  
  
#### CSV Format for Updating Building Capacity
CSV should be formatted as  
```[buildling],[new capacity]```  
with each building-capacity pair on a new line. 
