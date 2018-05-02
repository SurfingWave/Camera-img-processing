<?php
error_reporting(E_ALL); 
ini_set('display_errors', 1);

if ($_SERVER['REQUEST_METHOD']=='POST'){
    $img = $_POST['image'];
    require_once('dbConnect.php');
    $sql = "INSERT INTO imgs (img) VALUES (?)";
    $stmt = mysqli_prepare($con, $sql);
    mysqli_stmt_bind_param($stmt, "s", $img);
    mysqli_stmt_execute($stmt);
    $check = mysqli_stmt_affected_rows($stmt);

    if($check==1){
        echo nl2br("Success: Image Uploads\n");
    }else{
        echo nl2br("Error: Upload Image, Insert Row Failed\n");
    }
    mysqli_close($con);
}
else {
    echo nl2br("Failed: Image Upload, POST Please");
}