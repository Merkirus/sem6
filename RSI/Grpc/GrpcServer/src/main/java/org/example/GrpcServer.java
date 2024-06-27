package org.example;

import com.google.protobuf.ByteString;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GrpcServer {

    private static final List<Data> dataList = new ArrayList<>();

    static {
        dataList.add(Data.newBuilder()
                .setId(1)
                .setName("Mieszko")
                .setAge(12)
                .setImagePath("images.jpeg")
                .build());

        dataList.add(Data.newBuilder()
                .setId(2)
                .setName("Alicja")
                .setAge(5)
                .setImagePath("images1.jpeg")
                .build());

        dataList.add(Data.newBuilder()
                .setId(3)
                .setName("Sniezynka")
                .setAge(25)
                .setImagePath("images2.png")
                .build());
    }

    public static void main(String[] args) {
        int port = 50001;
        System.out.println("Starting gRPC server...");
        Server server = ServerBuilder.forPort(port)
                .addService(new MyServiceImpl()).build();
        try {
            server.start();
            System.out.println("...Server started");
            server.awaitTermination();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyServiceImpl extends ServiceNameGrpc.ServiceNameImplBase {
        @Override
        public void getData(Empty request, StreamObserver<Data> responseObserver) {
//            StringBuilder msg = new StringBuilder();

            for (Data d : dataList) {
//                String temp = String.format("Id: %d, Name: %s, Age: %d, ImagePath: %s ", d.getId(), d.getName(), d.getAge(), d.getImagePath());
//                msg.append(temp);
                responseObserver.onNext(d);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

//            Text message = Text.newBuilder().setMessage(msg.toString()).build();
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            responseObserver.onCompleted();
        }

        @Override
        public void getRecord(Id request, StreamObserver<Data> responseObserver) {
            String msg;

            Data data = dataList.stream().filter(d -> d.getId() == request.getId()).findAny().orElse(null);

//            if (data != null) {
//                msg = String.format("Id: %d, Name: %s, Age: %d, ImagePath: %s ", data.getId(), data.getName(), data.getAge(), data.getImagePath());
//            } else {
//                msg = "Data not found";
//            }
//
//            Text message = Text.newBuilder().setMessage(msg).build();

            responseObserver.onNext(data);
            responseObserver.onCompleted();
        }

        @Override
        public void setRecord(Data request, StreamObserver<Text> responseObserver) {
            String msg;

            boolean removed = dataList.removeIf(d -> d.getId() == request.getId());

            dataList.add(request);

            dataList.sort(Comparator.comparingInt(Data::getId));

            if (removed) {
                msg = "Updated record";
            } else {
                msg = "Added new record";
            }

            Text message = Text.newBuilder().setMessage(msg).build();

            responseObserver.onNext(message);
            responseObserver.onCompleted();
        }

        @Override
        public void countData(Data request, StreamObserver<Text> responseObserver) {
            String msg;

            long count = dataList.stream().filter(d -> d.getName().equals(request.getName())).count();

            msg = "Data with given name: " + count;

            Text message = Text.newBuilder().setMessage(msg).build();

            responseObserver.onNext(message);
            responseObserver.onCompleted();
        }

        @Override
        public StreamObserver<Image> uploadImage(StreamObserver<Text> responseObserver) {
                return new StreamObserver<Image>() {
                private final File file = new File("GrpcServer/src/main/java/org/example/images/output.png");
                private FileOutputStream fos;

                @Override
                public void onNext(Image value) {
                    try {
                        if (fos == null) {
                            fos = new FileOutputStream(file);
                        }
                        fos.write(value.getChunks().toByteArray());

                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Text response = Text.newBuilder()
                                .setMessage("Uploaded chunk ***")
                                .build();

//                        responseObserver.onNext(response);
                    } catch (IOException e) {
                        System.out.println("ONNEXT ERROR");
//                        throw new RuntimeException(e);
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
                        System.out.println("ONCOMPLETED ERROR");
//                        throw new RuntimeException();
                    }
                    Text response = Text.newBuilder()
                            .setMessage("Completed streaming picture")
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public void downloadImage(Text request, StreamObserver<Image> responseObserver) {
            String file_name = request.getMessage();
            File image = new File("GrpcServer/src/main/java/org/example/images/" + file_name);

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
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    responseObserver.onNext(img);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            responseObserver.onCompleted();
        }

        //        public void unaryProcedure(TheRequest req,
//                                   StreamObserver<TheResponse> responseObserver) {
//            String msg;
//            System.out.println("...called UnaryProcedure - start");
//            if (req.getAge() > 18)
//                msg = "Mr/Ms "+ req.getName();
//            else
//                msg = "Boy/Girl";
//            TheResponse response = TheResponse.newBuilder()
//                    .setMessage("Hello " + msg)
//                    .build();
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            responseObserver.onNext(response);
//            responseObserver.onCompleted();
//            System.out.println("...called UnaryProcedure - end");
//        }
//
//        @Override
//        public void streamProcedure(TheRequest req, StreamObserver<TheResponse> responseObserver) {
//            String msg;
//            System.out.println("...called streamProcedure - start");
//            if (req.getAge() > 18)
//                msg = "Mr/Ms "+ req.getName();
//            else
//                msg = "Boy/Girl";
//            int NUM_OF_CHUNKS = 5;
//            for( int i=0; i<NUM_OF_CHUNKS; i++) {
//                TheResponse response = TheResponse.newBuilder()
//                        .setMessage("Stream chunk " + (i+1)).build();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                responseObserver.onNext(response);
//            }
//            responseObserver.onCompleted();
//            System.out.println("...called streamProcedure - end");
//
//
//        }
//
//        @Override
//        public void streamBytesProcedure(TheRequest request, StreamObserver<ByteResponse> responseObserver) {
//            String file_name = request.getName();
//            File image = new File("GrpcServer/src/main/java/org/example/images/" + file_name + ".png");
//
//            try (FileInputStream fis = new FileInputStream(image)) {
//                int BUFF_SIZE = 1024;
//                byte[] byte_arr = new byte[BUFF_SIZE];
//
//                int data;
//                while ((data = fis.read(byte_arr)) != -1) {
//
//                    ByteResponse br = ByteResponse.newBuilder()
//                                    .setNumOfBytes(data)
//                                    .setChunks(ByteString.copyFrom(byte_arr, 0, data))
//                                    .build();
//
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    responseObserver.onNext(br);
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            responseObserver.onCompleted();
////            super.streamBytesProcedure(request, responseObserver);
//        }
//
//        @Override
//        public StreamObserver<TheRequest> streamToSrv(StreamObserver<TheResponse> responseObserver) {
//            StreamObserver<TheRequest> srvObserver = new StreamObserver<TheRequest>() {
//                @Override
//                public void onNext(TheRequest value) {
//                    System.out.println(value.getName());
//                }
//
//                @Override
//                public void onError(Throwable t) {
//                    System.out.println("Server observer error");
//                }
//
//                @Override
//                public void onCompleted() {
//                    TheResponse response = TheResponse.newBuilder()
//                    .setMessage("Hello server observer").build();
//                    responseObserver.onNext(response);
//                    responseObserver.onCompleted();
//                }
//            };
//            return srvObserver;
//            //return super.streamToSrv(responseObserver);
//        }
//
//        @Override
//        public StreamObserver<ByteRequest> streamToSrvBytes(StreamObserver<TheResponse> responseObserver) {
//            StreamObserver<ByteRequest> srvObserver = new StreamObserver<ByteRequest>() {
//                private File file = new File("GrpcServer/src/main/java/org/example/images/output.png");
//                private FileOutputStream fos;
//
//                @Override
//                public void onNext(ByteRequest value) {
//                    try {
//                        if (fos == null) {
//                            fos = new FileOutputStream(file);
//                        }
//                        fos.write(value.getChunks().toByteArray());
////                        TheResponse response = TheResponse.newBuilder()
////                                .setMessage("Streamed chunk ***")
////                                .build();
////                        responseObserver.onNext(response);
//                    } catch (IOException e) {
//                        System.out.println("ONNEXT ERROR");
////                        throw new RuntimeException(e);
//                    }
//                }
//
//                @Override
//                public void onError(Throwable t) {
//
//                }
//
//                @Override
//                public void onCompleted() {
//                    try {
//                        fos.flush();
//                        fos.close();
//                    } catch (IOException e) {
//                        System.out.println("ONCOMPLETED ERROR");
////                        throw new RuntimeException();
//                    }
//                    TheResponse response = TheResponse.newBuilder()
//                            .setMessage("Completed streaming picture")
//                            .build();
//                    responseObserver.onNext(response);
//                    responseObserver.onCompleted();
//                }
//            };
//            return srvObserver;
//        }
    }
}