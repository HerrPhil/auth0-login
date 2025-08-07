# auth0-login

This project is an evaluation project. I am evaluating the Auth0 offering for authentication.

As well, this project might be the basis of a fork to create an authentication module,
of several authentication solutions, that can be included in new app projects.

## The Details

I found this 2-part series of articles.

[part 1](https://auth0.com/blog/android-authentication-jetpack-compose-part-1/)

[part 2](https://auth0.com/blog/android-authentication-jetpack-compose-part-2/)

For a technical series, these articles are written in a way to make the reader/coder
succeed. It lays out all the details to achieve a working login screen.

This is refreshing. Articles that do a lot of hand-waving, and assume the reader will intuit
all the missing code are vexing.

The only bumps in the road, so far, are all reasonable.

For example, the publication date of the articles is February 23, 2023.

Then, in the current Coil3 release, the Coil project split out the network functionality into its
own library. That broke the article's load of the user profile PNG file. After the new import is
added, then the network domain name is resolvable.

Another small issue was the loading of the image. This bug was subtle. I was going back and forth
with this task, doing other things, and then returning to testing the app. I found that the wifi
reference of hte emulator goes stale when the app is stopped, and your computer goes to sleep, and
is woken up. In this case, the emulator must be restarted so that the asynchronous image load by
Coil can work.

