[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/vlo9idtn)
# lab1-wa2025

<h6> Maria Rita Franceschetti s322430 </h6>
<h6> Francesco Servente s328852</h6>
<h6> Giorgio Galasso s319497 </h6>
<h6> Marta Chiarenza s 328937</h6>



<h3>Do the following steps in the terminal in order to run the program:<h3>
<h4>
1. ./gradlew clean shadowJar
2. ls build/libs (per verificare se ha creato il file .jar)
3. docker build -t route-analyzer .
4. docker run -v \${PWD}/custom-parameters.yml:/app/custom-parameters.yml -v \${PWD}:/app route-analyzer
   <h4>


<h3> In order to test the project run the following command:</h3>
<h4> ./gradlew clean test </h4>