<?php
error_reporting(E_ALL); 
ini_set('display_errors', 1);

$upload_path = 'img_uploads/';
$server_ip = gethostbyname(gethostname());
$upload_url = 'http://'.$server_ip.'/'.$upload_path;

$response = array(); 
if ($_SERVER['REQUEST_METHOD']=='POST'){

    require_once('dbConnect.php');

    if(isset($_POST['name']) and isset($_FILES['image']['name'])){

        $name = $_POST['name'];
        $fileinfo = pathinfo($_FILES['image']['name']);
        $extension = $fileinfo['extension'];
    
        $file_url = $upload_url . getFileName($con) . '.' . $extension;
        $file_path = $upload_path . getFileName($con) . '.'. $extension; 

        try{
            //saving the file 
            move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
            $sql = "INSERT INTO `imgs` (`img_url`, `name`) VALUES ('$file_url', '$name');";
            
            //adding the path and name to database 
            if(mysqli_query($con,$sql)){
                //filling response array with values 
                $response['error'] = false; 
                $response['url'] = $file_url; 
                $response['name'] = $name;
            }
        }catch(Exception $e){
            $response['error']=true;
            $response['message']=$e->getMessage();
        } 

        //displaying the response 
        echo json_encode($response);
            
        //closing the connection 
        mysqli_close($con);
        }else{
            $response['error']=true;
            $response['message']='Please choose a file';
        }
}
else {
    echo nl2br("Failed: Image Upload, POST Please");
}

function getFileName($con){
    $sql = "SELECT max(id) as id FROM imgs";
    $result = mysqli_fetch_array(mysqli_query($con, $sql));
    if($result['id']==null)
        return 1; 
    else 
        return ++$result['id']; 
}