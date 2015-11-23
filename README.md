## installation

(ścieżki pisałem z pamięci, więc mogą się ciut różnić)

http://www.swi-prolog.org/build/unix.html
po instalacji skopiowałem plik `/usr/local/lib/*prolog*/lib/amd64/libjpl.so` do folderu javy `/usr/li/jvm/*java*/lib/amd64/`

a w folderze `/usr/local/lib/*prolog*/lib/` powinno być archiwum jpl.jar, które trzeba użyć lokalnie do projektu w javie/scali

dodatkowo zmienne środowiskowe (aczkolwiek nie wiem czy są potrzebne)
http://stackoverflow.com/questions/12283471/jpl-swi-prolog-configuration-failure

## uruchomienie

* zainstalować [sbt](http://www.scala-sbt.org/)
* w katalogu projektu wykonać `sbt run`