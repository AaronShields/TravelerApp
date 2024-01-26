# my_traveler_app_new


Welcome to Traveler - A Plan My Day App 

Overview:
- This app works on Android Studio Giraffe Version, please do not try to run it on Chipmunk (the firebase system does not work with that version at all)
We've initialized a username and password for you to use since you cannot directly access our firebase system: Username: guest@usc.edu Password: FightOn!

I recommend running the app in Pixel 2 API 24 until the view map section because for some reason the app works really well and runs cleanly on that emulator.

In order to see the google maps api on the app with the displayed information, use the Pixel 2 API 34 because the Pixel 2 API 24 does not have the capacity to 
view the map without being updated. 

Information for using the app
- App should build fairly quickly and has a simple flow process
  1) Log in to main page using credentials (Username: guest@usc.edu / Password: FightOn!)![Screenshot 2023-11-06 at 8 53 49 PM](https://github.com/nemzek/my_traveler_app_new/assets/70115915/1449f418-ff4b-4c78-8eb8-371e5e4f4b38)![Uploading Screenshot 2023-11-06 at 8.55.36 PM.png…]()
  2) Page redirects to choosing your tour location
  3) Choose USC or LA ![Screenshot 2023-11-06 at 8 55 55 PM](https://github.com/nemzek/my_traveler_app_new/assets/70115915/35db0915-2b97-44a9-9cde-b6635e208ce4)
  4) Scroll and click the checkboxes for on the viewcards for the events and choose buttons (One Day Trip) or (Multi-day)
  5) ![Screenshot 2023-11-06 at 8 56 10 PM](https://github.com/nemzek/my_traveler_app_new/assets/70115915/5f01a013-2dba-4102-b931-4163cd6a0c5d)
  6) The elapsed time, start time for every event will show, it works well on my computer but I think one of my friends was having an issue with the algorithm reading in the events
  wrong and thus the end times going over the required hours. 
  7) ![Screenshot 2023-11-06 at 8 28 24 PM](https://github.com/nemzek/my_traveler_app_new/assets/70115915/bd4a10bf-ca39-4c86-8eea-f74aa62770fb)
  8) Click on the view map button and the events will be shown on the google maps API!
  9) END
 
Some warnings: 
- Going back and forth between Event pages and Route Planning pages and adding and removing checkbox events can cause the algorithm for calculating time to act up
- Time is written in military time when it is displayed. 
- Elapsed time takes into account that the user might want to spread things out during the day.
- For example the chinese theater opens at 7:00, user spends 30 minutes there, adds 27 for travel to theater, so end time is 7:57. 
- However, La brea tar pits close at 18:00, so user will wait to travel to tar pits so they can leave (end time) at 17:50 which is 10 minutes before close.

Improved Capabilities: 
- Forgot My Password feature added
- Create Account feature added
- Sign out button added
- Multi-day event feature changed from 3 days to 20

