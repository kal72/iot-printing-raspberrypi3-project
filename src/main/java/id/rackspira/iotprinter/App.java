package id.rackspira.iotprinter;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import de.spqrinfo.cups4j.CupsClient;
import de.spqrinfo.cups4j.CupsPrinter;
import de.spqrinfo.cups4j.PrintJob;
import id.rackspira.iotprinter.helper.AppHelper;
import id.rackspira.iotprinter.helper.Constant;
import id.rackspira.iotprinter.model.PrintProfile;

import java.io.*;
import java.net.URL;
import java.util.Arrays;

public class App {

    public static void main(String[] args) {
        System.out.println("starting");
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Constant.PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(Constant.PUBNUB_PUBLISH_KEY);

        PubNub pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(subscribeCallbackPrintChannel());

        pubnub.subscribe().channels(Arrays.asList(Constant.PN_CHANNEL_PRINTING)).execute();

    }

    private static PNCallback<PNPublishResult> pnPublishResultPNCallback() {
        return new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
                if (!status.isError()) {

                } else {
                }
            }
        };
    }

    private static SubscribeCallback subscribeCallbackPrintChannel() {
        return new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                System.out.println(status.getCategory().toString());

//                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
//                } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
//                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
//
//                    }
//                } else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {
//                } else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {
//                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    PrintProfile printProfile = AppHelper.gson().fromJson(message.getMessage().toString(), PrintProfile.class);
                    printService(Constant.LINK +"/"+printProfile.getTitle());
                    System.out.println(printProfile.getTitle());
                } else {
                    System.out.println("Channer is null");
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        };
    }

    public static void printService(String link) {
        try {
            InputStream inputStream = new URL(link).openStream();
            System.out.println(inputStream.available());
            System.out.println(inputStream);
            CupsClient cupsClient = new CupsClient(Constant.CUPS_SERVER, Constant.CUPS_PORT);
            CupsPrinter cupsPrinter = cupsClient.getDefaultPrinter();
            PrintJob printJob = new PrintJob.Builder(inputStream).build();
            cupsPrinter.print(printJob);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Hello World! " + App.class.getProtectionDomain().getCodeSource().getLocation().getPath().toString());
    }


}
