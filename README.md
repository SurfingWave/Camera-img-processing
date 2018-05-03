# Image uploading android application + php server
## Image uploading android application
### Photo taking
- Click the button to take the photo using the system camera
- Store the photos to the system storage
### Image choosing
- Open the system file folder
- Choose the image to upload
### Image Uploading
- Preview the image to be uploaded
- Upload the image to remote server
### **TODO** Image fetching
- Fetch the image from server
- Preview the image on app
### **TODO** Analyzing Result Returning
- Get the analysing result from server
- Display the result on the application

## Php server

>|--**`img_upload.php`**:  Get the post request from android application store photo in directory `/img_uploads`, and store the `img_url` in the database.

>|--**`img_get.php`**: Retrieve the images in database, return the information, including the `id`, `date`, `img_url`, `name`.

>|--**`db_conn.php`**: Establish the database connection.

### MySQL Table Structure
|id | date| img_url| name|
| :-| :-: | :-: | :-:|
 30 | 2018-05-02 16:36:16 | http://127.0.0.1/img_uploads/30.jpg | toe      |
| 31 | 2018-05-02 16:38:59 | http://127.0.0.1/img_uploads/31.jpg | toe      |
| 32 | 2018-05-02 16:39:13 | http://127.0.0.1/img_uploads/32.jpg | toe      |
| 33 | 2018-05-02 17:30:36 | http://127.0.0.1/img_uploads/33.jpg | Test img |

**TODO** Backend image proceesing, including machine learning model



