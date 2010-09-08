couchmail
==================

Installerating
------
 * Get [leiningen](http://github.com/technomancy/leiningen)

    git clone ...
    cd couchmail
    lein deps
    #abuse nailgun:
    ./start.sh&

Usage
-------
Start a nailgun server (currently I develop with vimclojure and am lazy, so this is tied to the vimclojure nailgun server in vimclojure, but at least I pack my handy start.sh).

Send email to be stored to couchdb in MIME to the couchmail.Store Nail with the nailgun client.

Example .fetchmailrc:
    poll imap.mailserver.com protocol imap user "someguy@placeville.io" password "hunter2" mda "/usr/local/bin/ng couchmail.Store" ssl


