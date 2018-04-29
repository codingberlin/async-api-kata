# Async api kata

### Task

Fetch asynchronous data from an API:

The task is to write a script or app which gathers data from two endpoints asynchronously, merges the responses and displays them in any way in the browser.


For example the following two entpoints are used: 

[http://jsonplaceholder.typicode.com/users/1](http://jsonplaceholder.typicode.com/users/1) to obtain a user's data

[http://jsonplaceholder.typicode.com/posts?userId=1](http://jsonplaceholder.typicode.com/posts?userId=1) to obtain all comments written by that user

### Additional Assumptions

- There is some `Important Content` which should be shown even if the backend APIs are broken or deliver uncomplete data.
- The client side is static and no single page app
- The rendered content is SEO relevant

Because of the `Important Content` and to not distract the user I do not render error messages but just leave out the missing data. Of course the errors must be logged and monitoredin production.

### Client vs. Server Side

The kata could be implemented ether client side, server side or as a mix out of client and server side.

In my opinion you should avoid the mix of client and server side templating when ever possible or reduce the complexity on one of both sides to focus on one side. For this kata a mix is not necessary so I choose one of the other possibilities.

If the page would be a single page app, the content would not be SEO relevant and the APIs would be accessable via CORS you could implement this task with a static HTML file (which could be hosted on AWS S3) including JavaScript (better TypeScript) to retrieve the APIs as AJAX calls and display its content.

Because of my additional assumptions I prefere the server side templating over the client side templating and thus I do not use JavaScript at all.

### Run

To run the app

```bash
sbt run
```

After the app has been startet browse [http://localhost:9000/comments/1](http://localhost:9000/comments/1)

### Test

To run the functional tests

```bash
sbt test
```

I prefere to use functional tests whenever possible because they test in comparison to unit tests additionally the composition of the units. Another advantage is your freedom to refactor without the need to change the (unit) tests.

With commit 69893300f85372e59ed20608f6f4599adcaebb05 you can see refactoring for the UsersApi interface and the used case classes without adapting unit tests but still covered functionality with the functional tests.

According to use the right tool for the job, when one unit has big complexity I would exceptionally use a unit test for that unit in combination with one happy trail functional test because to write and to run functional tests for the big complexity would be ineffective in this case.

### ToDos

- handle uncomplete data for the posts API with `Option` e.g. if one post does have a `title` but no `body`.
- add timeouts: just deliver `Important Content` when the APIs are too slow
- apply style with CSS (was out of scope for the kata)
- add mechanism to create deployable artefact (e.g. sbt-native-packager)
- cleanup: remove/replace the initial generated stuff like the HomeController by a StatusController for monitoring purpose

