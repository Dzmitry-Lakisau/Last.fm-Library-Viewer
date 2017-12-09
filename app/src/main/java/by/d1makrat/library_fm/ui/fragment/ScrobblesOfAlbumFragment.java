package by.d1makrat.library_fm.ui.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.asynctask.GetScrobblesOfAlbumAsynctask;

public class ScrobblesOfAlbumFragment extends ScrobblesListFragment {

    private static final String ARTIST_KEY = "artist";
    private static final java.lang.String ALBUM_KEY = "track";
    private static String artist;
    private static String album;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artist = getArguments().getString(ARTIST_KEY);
        album = getArguments().getString(ALBUM_KEY);
        urlForBrowser = "https://www.last.fm/user/" + AppContext.getInstance().getUsername() + "/library/music/" + artist + "/" + album;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(artist);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(album);

        return rootView;
    }
//TODO check behaviour for limit>200

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(contextMenu, v ,menuInfo);

        contextMenu.getItem(2).setVisible(false);
    }

    @Override
    public void loadItemsFromWeb() {
        String[] asynctaskArgs = {artist, album, String.valueOf(mPage), mFrom, mTo};
        mGetScrobblesAsynctask = new GetScrobblesOfAlbumAsynctask(this);
        mGetScrobblesAsynctask.execute(asynctaskArgs);
    }
}
//    public class GetScrobblesOfAlbumTask extends AsyncTask<TreeMap<String, String>, Integer, ArrayList<HashMap<String, String>>> {
//
//        private View list_spinner = getActivity().getLayoutInflater().inflate(R.layout.list_spinner, (ViewGroup) null);
//        private int exception = 0;
//        private String message = null;
//        private ProgressBar progressBar = (ProgressBar) list_spinner.findViewById(R.id.progressbar);
//        private int pages_count = 0;
//
//        @Override
//        protected ArrayList<HashMap<String, String>> doInBackground(TreeMap<String, String>... params) {
//
//            ArrayList<HashMap<String, String>> Tracks = new ArrayList<>();
//            ArrayList<HashMap<String, String>> AllArtistTracks;
//            FileOutputStream out = null;
//            Bitmap image;
//            String filename = null;
//
//            try {
//                Data rawxml = new Data(params[0]);
//                if (rawxml.parseAttribute("lfm", "status").equals("failed"))
//                    throw new APIException(rawxml.parseSingleText("error"));
//
//                pages_count = rawxml.pageCountOfArtist(params[0]);
//
//                int j=1;
//                float prim = 0;
//                int divider = limit/pages_count;
//                while(j<=pages_count){
//                    if (isCancelled()) return null;
//                    float sec = (j-1)*divider;
//                    params[0].put("mPage", String.valueOf(j));
//                    rawxml = new Data(params[0]);
//                    AllArtistTracks = rawxml.getTracks(resolution);
//                    for (HashMap<String, String> item: AllArtistTracks
//                         ) {
//                        if (item.get("album").equals(album)){
//                            Tracks.add(item);
//                            prim = prim + (float) 1*divider/AllArtistTracks.size();
//                        }
//                        sec = sec + (float) 1*divider/AllArtistTracks.size();
//                        publishProgress((int) prim, (int) sec);
//                    }
//                    prim = prim + j*divider;
//                    j++;
//                }
//
//                File folder = new File(cachepath + File.separator + resolution + File.separator + "Albums");
//                if (!folder.exists()) {
//                    folder.mkdirs();
//                }
//                for (int i = 0; i < Tracks.size(); i++) {
//                    if (isCancelled()) return null;
//                    HashMap<String, String> item = Tracks.get(i);
//                    String temp = item.get("image");
//                    if (!temp.equals("")) {
//                        if (item.get("album") != null || !item.get("album").equals("")){
//                            filename = folder.getPath() + File.separator + params[0].get("artist").replaceAll("[\\\\/:*?\"<>|]", "_") + " - " + item.get("album").replaceAll("[\\\\/:*?\"<>|]", "_") + ".png";
//                        }
//                        else
//                            filename = folder.getPath() + File.separator + params[0].get("artist").replaceAll("[\\\\/:*?\"<>|]", "_") + ".png";
//                        if (!(new File(filename).exists())) {
//                            try {
//                                URL newurl = new URL(temp);
//                                image = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
//                                out = new FileOutputStream(filename);
//                                image.compress(Bitmap.CompressFormat.PNG, 100, out);
//                                out.flush();
//                                out.close();
//                            }
//                            catch (Exception e) {
//                                //FirebaseCrash.report(e);
//                                e.printStackTrace();
//                                File file = new File(filename);
//                                boolean deleted = file.delete();
//                            }
//                            finally {
//                                try {
//                                    if (out != null) {
//                                        out.close();
//                                    }
//                                } catch (IOException e) {
//                                    //FirebaseCrash.report(e);
//                                    e.printStackTrace();
//                                    exception = 3;
//                                }
//                            }
//                        }
//                    } else {
//                        filename = path_to_blank + File.separator + "blank_albumart.png";
//                    }
//                    item.put("image", filename);
//                    Tracks.set(i, item);
//                }
//            }
//            catch (XmlPullParserException e){
//                //FirebaseCrash.report(e);
//                e.printStackTrace();
//                exception = 9;
//            }
//            catch (UnknownHostException e) {
//                e.printStackTrace();
//                exception = 8;
//            }
//            catch (SocketTimeoutException e){
//                e.printStackTrace();
//                exception = 7;
//            }
//            catch (MalformedURLException e){
//                //FirebaseCrash.report(e);
//                e.printStackTrace();
//                exception = 6;
//            }
//            catch (SSLException e) {
//                //FirebaseCrash.report(e);
//                e.printStackTrace();
//                exception = 5;
//            }
//            catch (FileNotFoundException e){
//                e.printStackTrace();
//                exception = 4;
//            }
//            catch (RuntimeException e){
//                //FirebaseCrash.report(e);
//                e.printStackTrace();
//                exception = 3;
//            }
//            catch (IOException e){
//                //FirebaseCrash.report(e);
//                e.printStackTrace();
//                exception = 2;
//            }
//            catch (APIException e){
//                //FirebaseCrash.report(e);
//                message = e.getMessage();
//                exception = 1;
//            }
//            finally {
//                publishProgress(limit, limit);
//                try {
//                    if (out != null) {
//                        out.close();
//                    }
//                } catch (IOException e) {
//                    //FirebaseCrash.report(e);
//                    e.printStackTrace();
//                    exception = 3;
//                }
//            }
//            return Tracks;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
//            lView.removeFooterView(list_spinner);
//            isLoading = false;
//            wasEmpty = false;
//            if (exception == 0 && result.size() > 0){
//                list_head.setVisibility(View.VISIBLE);
//                for (int i = 0; i < result.size(); i++) {
//                    items.add(result.get(i));
//                }
//                adapter.notifyDataSetChanged();
//                list_head_text = "Scrobbles: " + lView.getCount() + ((filter_string == null) ? "" : " within " + filter_string);
//                ((TextView) list_head.findViewById(R.id.list_head)).setText(list_head_text);
//            }
//            if (exception == 0 && result.size() == 0 && lView.getCount() == 0) {
//                empty_list.setVisibility(View.VISIBLE);
//                empty_list_text = "No scrobbles" + ((filter_string == null) ? "" : "\nwithin " + filter_string);
//                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(empty_list_text);
//                wasEmpty = true;
//            }
//            if (exception == 1 && lView.getCount() == 0){
//                page--;
//                empty_list.setVisibility(View.VISIBLE);
//                empty_list_text = "Error occurred";
//                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(empty_list_text);
//                wasEmpty = true;
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//            }
//            if (exception == 1 && lView.getCount() > 0) {
//                page--;
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//            }
//            if (exception > 1 && lView.getCount() == 0){
//                page--;
//                empty_list.setVisibility(View.VISIBLE);
//                empty_list_text = "Error occurred";
//                ((TextView) empty_list.findViewById(R.id.empty_list_text)).setText(empty_list_text);
//                wasEmpty = true;
//                String[] exception_message = getResources().getStringArray(R.array.Exception_messages);
//                Toast.makeText(getContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
//            }
//            if (exception > 1 && lView.getCount() > 0) {
//                page--;
//                String[] exception_message = getResources().getStringArray(R.array.Exception_messages);
//                Toast.makeText(getContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        protected void onPreExecute(){
//            empty_list.setVisibility(View.GONE);
//            progressBar.setProgress(0);
//            progressBar.setMax(limit);
//            lView.addFooterView(list_spinner);
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values){
//            super.onProgressUpdate(values);
//            progressBar.setProgress((values[0]));
//            progressBar.setSecondaryProgress(values[1]);
//        }
//
//        @Override
//        protected void onCancelled() {
//            isLoading = false;
//            wasEmpty = false;
//        }
//    }
//}