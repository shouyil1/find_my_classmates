# Find My Classmates 

## Introduction

With the growing number of students at USC, there’s an increasing challenge for students to connect with peers enrolled in similar courses. The “Find My Classmates” app aims to create a platform where students can effortlessly identify and engage with peers in the same academic courses, fostering collaboration and information sharing.

#### System Functions:
- User Registration and Profile Management
- Class Selector for academic department and course selection
- Display of classmates for chosen courses
- Messaging feature for direct communication with classmates
- Class rating and feedback

The application might need to interface with USC’s student enrollment databases to verify course enrolment data. Additionally, it could integrate with the official USC student portal for seamless user experience.

The “Find My Classmates” application aims to not just be a utility tool but also to enhance the university’s objective of fostering a collaborative and engaged student community. The platform can also indirectly improve class participation and - peer-assisted learning, essential facets of USC’s pedagogical goals.

### 2.5 Sprint
Improved capabilities since Project 2.4:
According to the feedbacks for our Implementation, we added the blocking feature to fulfill the core functionality. 
The user can block/unblock someone in the chat room by clicking the block/unblock button at the bottom of the chat.
Now our app covers all functionality required by the customer.

### 2.3 Implementation
JDK: Android Studio Embedded JDK
SDK: compileSdk 33, minSdk 24, targetSdk 33
Emulator: Pixel 2 API 24


Testing Credentials:
username: testing1@usc.edu
password: 123456

username: testing2@usc.edu
password: 123456

You can use any of the provided existing accounts above to log in. You can also choose to sign up your own account by registering a new account.

After logging in, you can freely browse all the tabs. 
Tab1 Classes: Here, you can see the list of classes in each school/department. Tapping on the specific school will display the courses under that school/department, and tapping on a course name will show you the description of that particular class. Meanwhile, you can add/drop the class by tapping the ADD/DROP button.

Tab2 My Enrollment: Here, you will see a list of all your enrolled classes. By tapping on the name of the class, it will display two options - SEE FEEDBACKS and FIND. By tapping the Find button, you can see the list of classmates enrolled in that class. In there, you can navigate to the student's profile by clicking on their names. In the profile page, by the SEND MESSAGE button, you can send messages to that student and automatically initiate a chat window between you two.

Once you click the button SEE FEEDBACKS, you can see all the comments and ratings posted for this class. You can add your own feedback by tapping on the RATE CLASS button on the top. Once you've created your own feedbacks, you can choose to edit/delete your feedbacks by tapping the corresponding buttons. Note that you cannot edit/delete feedbacks created by other students. Upvotes/Downvotes options are available. 

Note that for testing purpose, the majority of students and feedbacks are created under CS310 class.

Tab3 Profile: Displaying your current profile, name, role, and profile picture. You can also edit those attributes. 

Inbox Button: on the bottom right corner, you will see an inbox button that will take you to another inbox page that stores the chats between you and your classmates. Taping in one chatroom, and you can start chatting with other classmates concurrently. 
