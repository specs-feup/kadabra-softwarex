echo off
java -jar ../kadabra.jar -c ../configs/SortAutotuner-simpleAlgs.config
java -jar ../eclipse-build.jar ../ -u ../repo.userlibraries --project SortWithAutotuner --build --main examples.Main
java -jar ./SortWithAutotuner.jar