fmt:
	sbt scalafmtSbt scalafmt test:scalafmt
	prototool format src/main/protobuf -d
	prototool format src/main/protobuf -w
