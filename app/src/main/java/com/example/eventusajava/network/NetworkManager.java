package com.example.eventusajava.network;

import static com.example.eventusajava.network.RequestType.*;
import static com.example.eventusajava.screens.addEvent.AddEditEventActivity.defaultDateFormat;

import android.os.AsyncTask;

import com.example.eventusajava.data.models.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings("unchecked")
public class NetworkManager {

    private static final String BASE_URL = "https://eventusamobile-production-api.azurewebsites.net";

    private static final String POST_EVENT_PATH = "/Event/new-event";
    private static final String GET_EVENTS_PATH = "/Event/event-list";
    private static final String UPDATE_EVENT = "/Event/update-event";
    private static final String DELETE_EVENT = "/Event/delete-event";
    private static final String LOGIN_PATH = "/User/login";


    public static void loginUser(String username, String password, Consumer<String> responseConsumer, Consumer<Exception> errorConsumer) {

        String s = "{\"username\":\"string\",\"pass\":\"string\"}";

        new sendRequestAsyncTask(LOGIN_PATH, s).execute((responseBody, exception) -> {
            if (exception != null) {
                errorConsumer.accept(exception);
                return;
            }

            responseConsumer.accept(responseBody);
        });
    }

    public static void getEvents(Consumer<List<Event>> eventsConsumer, Consumer<Exception> errorConsumer) {

        new sendRequestAsyncTask(GET_EVENTS_PATH, "").execute((responseBody, exception) -> {

            if (exception != null) {
                errorConsumer.accept(exception);
                return;
            }

            Type eventsType = new TypeToken<ArrayList<Event>>() {
            }.getType();

            List<Event> events = new Gson().fromJson(responseBody, eventsType);

            for(Event event: events){
                try{
                    event.setDateTo(defaultDateFormat.parse(event.getDateToString()));
                    event.setDateFrom(defaultDateFormat.parse(event.getDateFromString()));
                }catch(Exception ignored){
                    event.setDateToString("");
                    event.setDateFromString("");
                }

            }

            eventsConsumer.accept(events);
        });

    }

    public static void postEvent(Event event, Consumer<String> eventIdConsumer, Consumer<Exception> errorConsumer) {

        String eventString = new Gson().toJson(event);

        new sendRequestAsyncTask(POST_EVENT_PATH, eventString).execute((responseBody, exception) -> {
            if (exception != null) {
                errorConsumer.accept(exception);
            }

            String eventId = responseBody;
            eventIdConsumer.accept(eventId);
        });
    }

    public static void postDeleteEvent(int eventId, Consumer<Boolean> deleteEventConsumer) {
        new sendRequestAsyncTask(DELETE_EVENT + "?id=" + eventId, "")
                .execute((responseBody, exception) -> {
                    deleteEventConsumer.accept(responseBody.equals("true"));
                });
    }

    public static void updateEvent(Event event, Consumer<Boolean> updateEventConsumer, Consumer<Exception> errorConsumer) {

        String eventString = new Gson().toJson(event);

        new sendRequestAsyncTask(UPDATE_EVENT, eventString).execute((responseBody, exception) -> {
            if (exception != null) {
                errorConsumer.accept(exception);
                return;
            }

            updateEventConsumer.accept(responseBody.equals("true"));
        });
    }

    private static class sendRequestAsyncTask extends AsyncTask<BiConsumer<String, Exception>, Void, Void> {

        private String urlPath;
        private String requestBodyString;

        public sendRequestAsyncTask(String urlPath, String requestBody) {
            this.urlPath = urlPath;
            this.requestBodyString = requestBody;
        }

        @Override
        protected Void doInBackground(BiConsumer... biConsumer) {
            try {
                String response = sendRequest(urlPath, requestBodyString);
                biConsumer[0].accept(response, null);
            } catch (Exception exception) {
                biConsumer[0].accept(null, exception);
            }
            return null;
        }

        String sendRequest(String urlPath, String requestBodyString) throws Exception {

            RequestType requestType = getRequestMethodForPath(urlPath);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
            if (requestType == GET) {
                requestBody = null;
            }

//            requestBody = RequestBody.create(mediaType, "{\"eventId\":0,\"from\":\"2023-01-27T09:21:02.739Z\",\"to\":\"2023-01-27T09:30:02.739Z\",\"name\":\"string\",\"description\":\"string\",\"location\":\"string\",\"calendar\":true,\"dateAddded\":\"2023-01-27T09:21:02.739Z\",\"days\":0}");
            Request request = new Request.Builder()
                    .url(BASE_URL + urlPath)
                    .method(requestType.getLabel(), requestBody)
                    .addHeader("accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build();


            Response response = client.newCall(request).execute();
//            ResponseBody responseBody = response.body();

//            long a = responseBody.contentLength();

            return response.body().string();

        }

    }

    private static RequestType getRequestMethodForPath(String path) {

        if(path.contains(DELETE_EVENT)) return POST;

        switch (path) {
            case GET_EVENTS_PATH:
                return GET;
            case POST_EVENT_PATH:
            case UPDATE_EVENT:
                return POST;
        }
        return null;
    }

}
