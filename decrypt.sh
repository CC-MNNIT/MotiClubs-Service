#!/bin/sh

# --batch to prevent interactive command
# --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="$SEC" \
--output src/main/resources/firebase_private_key.json src/main/resources/firebase_private_key.json.gpg

gpg --quiet --batch --yes --decrypt --passphrase="$SEC" \
--output src/main/resources/secrets.yml src/main/resources/secrets.yml.gpg
