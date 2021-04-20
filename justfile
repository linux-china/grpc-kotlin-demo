services:
  evans -r -p 50052 cli list

call:
  echo '{ "name": "ktr" }' | evans -r -p 50052 cli call org.mvnsearch.grpc.GreeterService.SayHello
