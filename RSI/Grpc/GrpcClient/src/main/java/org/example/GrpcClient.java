package org.example;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

public class GrpcClient {
    public static void main(String[] args) {
        System.out.println("Running gRPC client...");

        var scanner = new Scanner(System.in);

        String input;

        do {

            System.out.println("Press 1 to getData");
            System.out.println("Press 2 to getRecord");
            System.out.println("Press 3 to setRecord");
            System.out.println("Press 4 to countData");
            System.out.println("Press 5 to uploadImage");
            System.out.println("Press 6 to downloadImage");
            System.out.println("Press q to quit");

            input = scanner.nextLine();

            switch (input) {
                case "1": {
                    getData();
                    break;
                }
                case "2": {
                    System.out.println("Enter id");
                    String id = scanner.nextLine();
                    getRecord(Integer.parseInt(id));
                    break;
                }
                case "3": {
                    System.out.println("Enter id");
                    String id = scanner.nextLine();
                    System.out.println("Enter name");
                    String name = scanner.nextLine();
                    System.out.println("Enter age");
                    String age = scanner.nextLine();
                    System.out.println("Enter imagePath");
                    String img = scanner.nextLine();
                    setRecord(Integer.parseInt(id), name, Integer.parseInt(age), img);
                    break;
                }
                case "4": {
                    System.out.println("Enter name");
                    String name = scanner.nextLine();
                    countData(name);
                    break;
                }
                case "5": {
                    System.out.println("Enter imagePath");
                    String img = scanner.nextLine();
                    uploadImage(img);
                    break;
                }
                case "6": {
                    System.out.println("Enter imagePath");
                    String img = scanner.nextLine();
                    downloadImage(img);
                    break;
                }
                default: {
                    break;
                }
            }

        } while (!input.equals("q"));
    }

    public static void getData() {
        String address = "localhost"; //here we use service on the same host
        int port = 50001;
        ServiceNameGrpc.ServiceNameStub nonbStub;
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(address, port) .usePlaintext().build();
        nonbStub = ServiceNameGrpc.newStub(channel);
        Empty empty = Empty.newBuilder().build();
        System.out.println("...calling getData");
        try {
            nonbStub.getData(empty, new DataObs());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("...after calling getData");
//        System.out.println("--> Response: " + response);
        channel.shutdown();
    }

    public static void getRecord(int id) {
        String address = "localhost"; //here we use service on the same host
        int port = 50001;
        ServiceNameGrpc.ServiceNameBlockingStub bStub;
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(address, port) .usePlaintext().build();
        bStub = ServiceNameGrpc.newBlockingStub(channel);
        Id _id = Id.newBuilder().setId(id).build();
        System.out.println("...calling getRecord");
        Data d = bStub.getRecord(_id);
        String response = String.format("Id: %d, Name: %s, Age: %d, ImagePath: %s ", d.getId(), d.getName(), d.getAge(), d.getImagePath());
        System.out.println("...after calling getRecord");
        System.out.println("--> Response: " + response);
        channel.shutdown();
    }

    public static void setRecord(int id, String name, int age, String filePath) {
        String address = "localhost"; //here we use service on the same host
        int port = 50001;
        ServiceNameGrpc.ServiceNameBlockingStub bStub;
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(address, port) .usePlaintext().build();
        bStub = ServiceNameGrpc.newBlockingStub(channel);
        Data data = Data.newBuilder()
                .setId(id)
                .setName(name)
                .setAge(age)
                .setImagePath(filePath)
                .build();
        System.out.println("...calling setRecord");
        Text response = bStub.setRecord(data);
        System.out.println("...after calling setRecord");
        System.out.println("--> Response: " + response);
        channel.shutdown();
    }

    public static void countData(String name) {
        String address = "localhost"; //here we use service on the same host
        int port = 50001;
        ServiceNameGrpc.ServiceNameStub nonbStub;
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(address, port) .usePlaintext().build();
        nonbStub = ServiceNameGrpc.newStub(channel);
        Data data = Data.newBuilder().setName(name).build();
        System.out.println("...calling async countData");
        nonbStub.countData(data, new AsyncObs());
        System.out.println("...after calling async countData");

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        channel.shutdown();
    }

    public static void uploadImage(String filePath) {
        String address = "localhost"; //here we use service on the same host
        int port = 50001;
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(address, port) .usePlaintext().build();
        ServiceNameGrpc.ServiceNameStub nonbStub;
        nonbStub = ServiceNameGrpc.newStub(channel);
        StreamObserver<Image> byteObserver = nonbStub.uploadImage(new UploadObs());

        File image = new File("GrpcClient/src/main/java/org/example/images/" + filePath);

        try (FileInputStream fis = new FileInputStream(image)) {
            int BUFF_SIZE = 1024;
            byte[] byte_arr = new byte[BUFF_SIZE];

            int data;

            while ((data = fis.read(byte_arr)) != -1) {

                Image img = Image.newBuilder()
                        .setNumOfBytes(data)
                        .setChunks(ByteString.copyFrom(byte_arr, 0, data))
                        .build();

                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                byteObserver.onNext(img);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        byteObserver.onCompleted();

        try {
            Thread.sleep(2000);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        channel.shutdown();
    }

    public static void downloadImage(String filePath) {
        String address = "localhost"; //here we use service on the same host
        int port = 50001;
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(address, port).usePlaintext().build();
        ServiceNameGrpc.ServiceNameStub nonbStub;
        nonbStub = ServiceNameGrpc.newStub(channel);
        Text request = Text.newBuilder().setMessage(filePath).build();
        System.out.println("...async calling downloadImage");
        try {
            nonbStub.downloadImage(request, new DownloadObs());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("...after async calling downloadImage");

        channel.shutdown();
    }

//    public static void synchCall() {
//        String address = "localhost"; //here we use service on the same host
//        int port = 50001;
//        ServiceNameGrpc.ServiceNameBlockingStub bStub;
//        System.out.println("Running gRPC client...");
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress(address, port) .usePlaintext().build();
//        bStub = ServiceNameGrpc.newBlockingStub(channel);
//        TheRequest request = TheRequest.newBuilder().setName("Rafal")
//                .setAge(33).build();
//        System.out.println("...calling unaryProcedure");
//        TheResponse response = bStub.unaryProcedure(request);
//        System.out.println("...after calling unaryProcedure");
//        System.out.println("--> Response: " + response);
//        channel.shutdown();
//    }
//
//    public static void synchCallStream() {
//        String address = "localhost"; //here we use service on the same host
//        int port = 50001;
//        ServiceNameGrpc.ServiceNameBlockingStub bStub;
//        System.out.println("Running gRPC client...");
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress(address, port) .usePlaintext().build();
//        bStub = ServiceNameGrpc.newBlockingStub(channel);
//        TheRequest request = TheRequest.newBuilder().setName("Rafal")
//                .setAge(33).build();
//        System.out.println("...calling streamProcedure");
//        Iterator<TheResponse> responseIterator;
//        responseIterator = bStub.streamProcedure(request);
//        System.out.println("...after calling streamProcedure");
//        TheResponse strResponse;
//        while(responseIterator.hasNext()) {
//            strResponse = responseIterator.next();
//            System.out.println("-->" + strResponse.getMessage());
//        }
//        channel.shutdown();
//    }
//
//
//
//    public static void asynchCall() {
//        String address = "localhost"; //here we use service on the same host
//        int port = 50001;
//        System.out.println("Running gRPC client...");
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress(address, port) .usePlaintext().build();
//        ServiceNameGrpc.ServiceNameStub nonbStub;
//        nonbStub = ServiceNameGrpc.newStub(channel);
//        TheRequest request = TheRequest.newBuilder().setName("Rafal")
//                .setAge(33).build();
//        System.out.println("...async calling unaryProcedure");
//        nonbStub.unaryProcedure(request, new UnaryObs());
//        System.out.println("...after async calling unaryProcedure");
//
//        var scanner = new Scanner(System.in);
//        System.out.println("Press ENTER to continue");
//        scanner.nextLine();
//
//        channel.shutdown();
//    }
//
//    public static void asynchCallStream() {
//        String address = "localhost"; //here we use service on the same host
//        int port = 50001;
//        System.out.println("Running gRPC client...");
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress(address, port) .usePlaintext().build();
//        ServiceNameGrpc.ServiceNameStub nonbStub;
//        nonbStub = ServiceNameGrpc.newStub(channel);
//        TheRequest request = TheRequest.newBuilder().setName("Rafal")
//                .setAge(33).build();
//        System.out.println("...async calling streamProcedure");
//        nonbStub.streamProcedure(request, new UnaryObs());
//        System.out.println("...after async calling streamProcedure");
//
//        var scanner = new Scanner(System.in);
//        System.out.println("Press ENTER to continue");
//        scanner.nextLine();
//
//        channel.shutdown();
//    }
//    public static void requestServer() {
//        String address = "localhost"; //here we use service on the same host
//        int port = 50001;
//        System.out.println("Running gRPC client...");
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress(address, port) .usePlaintext().build();
//        ServiceNameGrpc.ServiceNameStub nonbStub;
//        nonbStub = ServiceNameGrpc.newStub(channel);
//        StreamObserver<TheRequest> strReqObserver = nonbStub.streamToSrv(new StrObs2());
//        String[] temp = {"Hej", "Ho", "Do", "Pracy", "By", "Sie", "Szlo"};
//        for (String t : temp) {
//            TheRequest req = TheRequest.newBuilder().setName(t).setAge(33).build();
//            strReqObserver.onNext(req);
//        }
//        strReqObserver.onCompleted();
//        try {
//            Thread.sleep(10000);
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        channel.shutdown();
//    }
//
//    public static void requestServerBytes() {
//        String address = "localhost"; //here we use service on the same host
//        int port = 50001;
//        System.out.println("Running gRPC client...");
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress(address, port) .usePlaintext().build();
//        ServiceNameGrpc.ServiceNameStub nonbStub;
//        nonbStub = ServiceNameGrpc.newStub(channel);
//        StreamObserver<ByteRequest> byteObserver = nonbStub.streamToSrvBytes(new StrObsBytes());
//
//        File image = new File("GrpcClient/src/main/java/org/example/images/output.png");
//
//        try (FileInputStream fis = new FileInputStream(image)) {
//            int BUFF_SIZE = 1024;
//            byte[] byte_arr = new byte[BUFF_SIZE];
//
//            int data;
//
//            while ((data = fis.read(byte_arr)) != -1) {
//
//                ByteRequest byteRequest = ByteRequest.newBuilder()
//                        .setNumOfBytes(data)
//                        .setChunks(ByteString.copyFrom(byte_arr, 0, data))
//                        .build();
//
//                try {
//                    Thread.sleep(100);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                byteObserver.onNext(byteRequest);
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//
//        byteObserver.onCompleted();
//
//        try {
//            Thread.sleep(10000);
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        channel.shutdown();
//    }
//
//    public static void pictureStream() {
//        String address = "localhost"; //here we use service on the same host
//        int port = 50001;
//        System.out.println("Running gRPC client...");
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress(address, port).usePlaintext().build();
//        ServiceNameGrpc.ServiceNameStub nonbStub;
//        nonbStub = ServiceNameGrpc.newStub(channel);
//        TheRequest request = TheRequest.newBuilder().setName("images2")
//                .setAge(33).build();
//        System.out.println("...async calling streamProcedure");
//        try {
//            nonbStub.streamBytesProcedure(request, new ByteObs());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("...after async calling streamProcedure");
//
//        var scanner = new Scanner(System.in);
//        System.out.println("Press ENTER to continue");
//        scanner.nextLine();
//
//        channel.shutdown();
//    }


//    private static class UnaryObs implements StreamObserver<TheResponse> {
//        public void onNext(TheResponse theResponse) {
//            System.out.println("-->async unary onNext: " +
//                    theResponse.getMessage());
//        }
//        public void onError(Throwable throwable) {
//            System.out.println("-->async unary onError");
//        }
//        public void onCompleted() {
//            System.out.println("-->async unary onCompleted");
//        }
//    }

    private static class AsyncObs implements StreamObserver<Text> {
        public void onNext(Text theResponse) {
            System.out.println("-->async unary onNext: " +
                    theResponse.getMessage());
        }
        public void onError(Throwable throwable) {
            System.out.println("-->async unary onError");
        }
        public void onCompleted() {
            System.out.println("-->async unary onCompleted");
        }
    }

    private static class UploadObs implements StreamObserver<Text> {

        @Override
        public void onNext(Text value) {
            System.out.println(value.getMessage());
        }

        @Override
        public void onError(Throwable t) {
            System.out.println("Error uploading an image");
        }

        @Override
        public void onCompleted() {
            System.out.println("Image uploading completed");
        }
    }

    private static class DownloadObs implements StreamObserver<Image> {
        private File output_file = new File("GrpcClient/src/main/java/org/example/images/output.png");
        private FileOutputStream fos;

        @Override
        public void onNext(Image value) {
            try {
                if (fos == null) {
                    fos = new FileOutputStream(output_file);
                }
                int number_of_bytes = value.getNumOfBytes();
                fos.write(value.getChunks().toByteArray());

                System.out.println("Downloaded chunk ***");

                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onCompleted() {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Completed streaming picture");
        }
    }

    private static class DataObs implements StreamObserver<Data> {

        @Override
        public void onNext(Data value) {
            String response = String.format("Id: %d, Name: %s, Age: %d, ImagePath: %s ", value.getId(), value.getName(), value.getAge(), value.getImagePath());
            System.out.println(response);
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onCompleted() {
            System.out.println("Completed streaming data");
        }
    }
}