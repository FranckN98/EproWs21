# How to use the Hoppscotch collection
To use the Hoppscotch collection, you first must open 
[hoppscotch.io](hoppscotch.io), what you see here is already the
hoppscotch client.

As this project is not deployed anywhere currently, you also must install
the Hoppscotch Extension for [Firefox](https://addons.mozilla.org/de/firefox/addon/hoppscotch/) or [Chrome](https://chrome.google.com/webstore/detail/hoppscotch-browser-extens/amknoiejhlmhancpahfcfcfhllgkpbld).

With the extension installed the next step is to import the collection into
Hoppscotch. You can do this by navigating to the Collections Tab in the sidebar, which is
to the right of the query-composer. By clicking the top right Button (next to the circled
question mark) you will be taken to a context menu, where you choose the "Import from 
Hoppscotch" option to import the `hoppscotchCollection.json` file.

When the Collection is imported you will see a folder called `Epro` with
several subfolders, which contain the prepared requests for all entities.

Next you need to set the Environment Variables, which are used in the requests
of the collection. For this you navigate to the `Environments` tab on the right sidebar
and in the `Global` Environment (or any other environment) you add the
`base_url` and the `base_path` variables.
The default value, which should be set for the `base_url` variable is `http://localhost:8080`
and the default value for the `base_path` is `/api/v1`.
When making a request to the `/login` endpoint the `bearer` environment variable is set automatically so that you
do not need to set this variable manually.

If you have the Application as well as the DB-Docker Container running, you
can now use Hoppscotch to send requests to the Application.