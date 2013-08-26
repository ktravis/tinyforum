tinyforum
=========

About
-----
TinyForum is a [Noir](http://webnoir.org)-based, "light-weight" forum system written in Clojure.

The project was intended to be extremely minimal, an exercise for learning Noir,
but it quickly exceeded the expectated feature set (and size.) It was heavily
inspired by [ForumFive](http://github.com/Xeoncross/forumfive) and borrows the
same structure and style, though in the interest of being more robust it weighs
in at < 5 MB total, rather than 5 kB. 

TinyForum uses the [Redis](http://redis.io) key-value system to store user
information and posts (via [aleph](https://github.com/ztellman/aleph)), with session-based authentication through Noir. [Markdown](http://daringfireball.net/projects/markdown/) is used for static site configuration, and parsing post and comment contents.

Twitter Bootstrap is used for styling, and the awesome
[SyntaxHighlighter!](http://alexgorbatchev.com/SyntaxHighlighter/)

Setup
-----
The simplest way to get started is with [Heroku](http://heroku.com), using an addon
such as [rediscloud](http://redis-cloud.com) or [redistogo](http://redistogo.com) to provide storage hosting. Otherwise, you can run a development version easily from your own machine, or a Vagrant vm.

####Without Heroku
Clone down this repo: `git clone https://github.com/ktravis/tinyforum`, and `cd`
into it. Install [Leiningen](http://leiningen.org) (v2.0.0 or greater) and run
`lein deps` to pull down project dependencies. Make sure you have a redis-server
up -- if not on the default port, follow the instructions below to configure.
The standard settings look for a redis server on localhost:6379 with no
password.

Now `lein run`, and you should be able to view your forum at `localhost:8080`,
as specified by the console output.

####Heroku
As above, clone down the repo and navigate to its root folder. Set up a new
Heroku app with `heroku apps:create` (assuming you have an account and the
heroku toolbelt set up.) Choose a redis provider, and add it to your app from
the Heroku dashboard, or `heroku addons:add redistogo:nano`. Wait about
a minute, and get your redis configuration: `heroku config | grep REDISTOGO_URL`
You should see something of the form redis://redistogo:PASSWORD@URL:PORT/. You
will need to edit the file `tinyforum/src/tinyforum/models/client.clj` so that
it reads:

```clojure
(ns tinyforum.models.client
     (:use [aleph.redis :only (redis-client)]))

(def redis-host "URL")
(def redis-password "PASSWORD")
(def redis-port PORT)

(def r (delay (redis-client 
  {:host redis-host
   :port redis-port
   :password redis-password})))
```

With the variable definitions in caps using the config parameters previously obtained.

Then simply `git add .; git commit -m "redis params"; git push heroku master` to
send your files to the Heroku server. The first build process could take
a while, so be patient. When it ends, you should be presented with a url
(something like `peaceful-ocean-4289.herokuapp.com`) with your forum ready to
go!

Customization
-------------
Once you're on the site, visit the login page to register a new account. To
promote your account to administrator, log out, and re-register with the
username "test@test.com" (unless you changed the default admin in
models/users.clj.) Then visit `/manage` on the forum, and promote your
previously created account. From the `/manage` page as an administrator, you may
change the site masthead, the main page title and description, the faq page, and
the footer. All except the masthead and title use
[Markdown](http://daringfireball.net/projects/markdown/) (via
[markdown-clj](https://github.com/yogthos/markdown-clj)) for formatting.
Administrators may all change these settings, as well as demote or delete other
user accounts, and remove topics and comments (or edit topics.)

License
-------
Copyright (C) 2013 Kyle Travis, all under the [MIT
License](http://ktravis.mit-license.org).
