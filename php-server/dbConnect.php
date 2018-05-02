<?php
error_reporting(E_ALL); 
ini_set('display_errors', 1);

define("HOST", "127.0.0.1");
define("USER", "root");
define("PASS", "wyk19256700");
define("DB", "imgs");
$con = mysqli_connect(HOST, USER, PASS, DB); 
if(!$con){
    die("Connection failed: " . mysqli_connect_error());
}
else{
    echo nl2br("Database Connection Success");
}
