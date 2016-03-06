# react-native-contacts

A react native library for reading contacts (Android)

This library is in development phase. So expect breakable changes.

#Installation
Just clone this repo and put this in node_modules folder.

### API
getContacts(callback) - returns all contacts 

### Usage
```javascript
import  {
    NativeModules
} from 'react-native';

var Contacts = NativeModules.ContactsAndroid;
Contacts.getContacts(function (contacts) {
})
```

### Contact Record's Example
```javascript
[
  {
    "contactId": "",
    "prefix": "",
    "middleName": "",
    "familyName": "",
    "givenName": "",
    "suffix": "",
    "displayName": "",
    "thumbnailPath":"",
    "phoneNumbers": [
      "",""
    ]
  }
]
```

### Getting Started
* In android/setting.gradle
  ```javascript
  ...
include ':react-native-contacts'
project(':react-native-contacts').projectDir = new File(settingsDir, '../node_modules/react-native-contacts/android')
```

* In android/app/build.gradle
```javascript
...
dependencies {
    ...
    compile project(':react-native-contacts')
}
```  

* In MainActivity.java

```javascript
  ...
import com.rkb.contactsreader.*;  <-- Import Package


public class MainActivity extends ReactActivity {

 ...

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new ReactNativeContactsPackage() <-- Include the package here
      );
    }
}
```
* add Contacts permission (in android/app/src/main/AndroidManifest.xml)
```javascript
<uses-permission android:name="android.permission.READ_CONTACTS" />
```
### Special Notes

The approach taken in this module relies heavily on @rt2zz and @morenoh149 comments at https://github.com/rt2zz/react-native-contacts/issues/38.





  

