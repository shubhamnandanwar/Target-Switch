# Target-Switch
Target Switch with animation
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Download](https://img.shields.io/badge/download-1.5-brightgreen.svg?style=flat) ](https://github.com/shubhamnandanwar/Target-Switch/releases/tag/1.0)

![Preview basic functionality screenshot](https://raw.githubusercontent.com/shubhamnandanwar/Target-Switch/master/art/target_switch.png)

A switch for showing/hiding target with cool dart animation.

## Preview
![Preview basic functionality screenshot](https://raw.githubusercontent.com/shubhamnandanwar/Target-Switch/master/art/preview.gif)


## Prerequisites
Add this to your project build.gradle
``` gradle
allprojects {
    repositories {
        jcenter()
    }
}
```
## Dependency
Add this to your module build.gradle

``` gradle
dependencies {
    implementation 'com.github.shubhamnandanwar:Target-Switch:1.5'
}
```

## Example
``` 
  <com.shunan.TargetSwitch.TargetSwitch
        android:id="@+id/targetSwitch"
        android:layout_width="120dp"
        android:layout_height="64dp"
        app:ts_background_padding="4dp"
        app:ts_background_tint="#BDBDBD"
        app:ts_duration="2500"
        app:ts_foreground_tint="#FFCDD2" />
```


### Attributes
Attributes | Type                  | Default         | Description
--- |-----------------------|-----------------| ---
ts_checked | Boolean               | false           | Switch Check
ts_duration | Integer               | 500             | Switch animation duration in milliseconds.
ts_background_tint | Color                 | #DDDDDD         | background color when the switch is off.
ts_foreground_tint | Color                 | #FFCDD2         | foreground color when the switch is on.
ts_background_padding | Dimension             | 0dp             | padding between switch and background


License
----
Copyright 2018 Shubham Nandanwar

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
