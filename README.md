couchmail
==================

Installerating
------
 * Get [cake](http://github.com/ninjudd/cake)

<pre>
git clone ...
cd couchmail
cake compile 
./start.sh &
</pre>

Usage
-------
Start a nailgun server (currently I develop with vimclojure and am lazy, so this is tied to the vimclojure nailgun server in vimclojure, but at least I pack my handy start.sh).

Send email to be stored to couchdb in MIME to the couchmail.Store Nail with the nailgun client (brew install nailgun, dude).

Example .fetchmailrc:
    poll imap.mailserver.com protocol imap user "someguy@placeville.io" password "hunter2" mda "/usr/local/bin/ng couchmail.Store http://127.0.0.1:5984/mail" ssl

couchmail.Store optionally excepts as a third parameter an aribtrary json blob to merge with imported mail. For example, to tag all mail from my work email account in a [mailbag](http://github.com/apage43/mailbag) bag, I can use a fetchmailrc line like this:
    poll imap.mailserver.com protocol imap user "someguy@workplace.com" password "hunter2" mda "/usr/local/bin/ng couchmail.Store http://127.0.0.1:5984/mail '{\x22bags\x22:[\x22work\x22]}'" ssl
