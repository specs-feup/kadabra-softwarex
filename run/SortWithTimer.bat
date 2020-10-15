echo off
java -jar ../kadabra.jar -c ../configs/TimeSort.config
java -jar ../eclipse-build.jar ../ -u ../repo.userlibraries --project SortWithTimer --build --main examples.Main
java -jar ./SortWithTimer.jar