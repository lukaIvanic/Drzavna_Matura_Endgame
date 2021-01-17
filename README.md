# Drzavna_Matura_Endgame
Premise -> A coming soon platform for high school students of Croatia. Includes materials for studying, tests, a forum and all that jazz.

Language: Java

Technologies: Firebase, Firebase Authentication, Firebase Storage

Database: Cloud Firestore

Functionalities: Exams, Studying, Dynamic quizzes, Login, Score system, Forum(Chat),  Search, Leaderboards, Background music, Daily reminder


# Exams

The user has the ability to try out an official exam from the previous years. These exams and their questions are inserted in the database manually. There are multiple types of questions which are all compatible with the recycler's adapter. The user can turn the exam in anytime he/she wants and they will get back all the right answers (and their answers if they're wrong). After submiting they can see their grade and an answer button on each question which either gives the answer in the format of a dialog or forwards them to a page in a script (done automatically). The question they didn't answer have added text "Not answered" in them. If a question has an image attached to it, the user can click on it and see it in full screen.

//TODO: Image(s)


### TODOs:

#1 Add a system that automatically adds all the available exams from the previous years.

#2 Add a zoom capability to the images attached to quesitons

#3 Store the users exam attempts and visually represent the best try on the Exams activity

#4 Add comments on individual questions/exams


# Studying

For now there are only scripts available.


### TODOs:

#1 Add search functionality in the scripts

#2 Make individual lessons


# Dynamic quizzes

The user can play a quiz against another user. The questions are randomly picked from a large set of question made with variations of the questions from the exams. The first user to get 5 answers right wins and gains points, while the loser loses points. 

//TODO: IMAGES


### TODOs:

#1 Add single player quizzes

#2 Add option to choose topic (Math, English, Croatian..)


# Login 

Login system using Firebase authentication. The user is automatically logged in after the first login. The user can also change their profile picture at any time. The profile pictures are stored in the Firebase Storage

//TODO: PICTURES

### TODOs:

#1 Give the user an option to turn of automatic login.

#2 An option to use the app as a guest

#3 Add a referral capability


# Score system:

Every user has his score which will be used for a shop and much more in the future...


# Forum(Chat)

Users have the ability to chat on a single thread.


# Search

The user is able to search lessons, curriculums and users all at once. 


# Leaderboards

The leaderboards show top 30 users with the highest scores.


# Background music

The app plays a lofi song on repeat on all activites. The user can turn on/off music in the settings. 


## Background music TODOs:

#1 Add more songs

#2 Find a way to not add 30+ MB to the app with each song


# Daily reminder

The user has the ability to turn on a daily reminder, which they set the time of themselves.
