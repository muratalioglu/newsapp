import { Button, Stack, TextField } from "@mui/material";
import { useState } from "react";
import Navbar from "./Navbar";

const CreateNews = () => {

    const [news, setNews] = useState({
        title: "",
        content: "",
        imageUrl: ""
    })

    const [selectedImageFile, setSelectedImageFile] = useState({
        file: "",
        preview: ""
    });

    const handleTitleInputChange = (e) => {        
        setNews(            
            (news) => ({
                ...news,
                title: e.target.value
            })
        )
    };

    const handleContentInputChange = (e) => {
        setNews(
            (news) => ({
                ...news,
                content: e.target.value
            })
        )
    };

    const handleImageInputChange = (e) => {
        if (e.target.files.length) {
            setSelectedImageFile(
                () => ({
                    file: e.target.files[0],
                    preview: URL.createObjectURL(e.target.files[0])
                })
            )
        } else {
            setSelectedImageFile(
                () => ({
                    file: "",
                    preview: ""
                })
            )
        }
    };

    const uploadImageFile = (e) => {
        
        e.preventDefault();

        const formData = new FormData();
        formData.append("file", selectedImageFile.file)

        return fetch(`http://localhost:8080/files`,
            {
                method: "POST",
                body: formData
            }
        );
    };

    const sendNewsForm = async (e) => {

        const imageUploadResponse = await uploadImageFile(e);

        let createNewsResponse = await fetch(`http://localhost:8080/news`, 
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    "title": news.title,
                    "content": news.content,
                    "imageUrl": imageUploadResponse.headers.get("location")
                })
            }
        );

        if (createNewsResponse.status === 409)
            console.log("The title is in use");
        
        let newsId = createNewsResponse.headers.get("NewsId");
        window.location.replace(`/news/${newsId}`);
    }

    return (
        <div>
            <Navbar />
            <h3>Create News</h3>
            <form onSubmit={sendNewsForm}>
            <Stack spacing={2}>
                <TextField id="title" label="Title" variant="outlined" value={news.title} onChange={handleTitleInputChange} />
                <TextField id="content" label="Content" multiline fullWidth rows={10} value={news.content} onChange={handleContentInputChange} />
            </Stack>
            <p>
                <Button variant="contained" component="label">
                    Choose Image
                    <input hidden type="file" accept="image/*" onChange={handleImageInputChange} />
                </Button>
            </p>
            <p><Button variant="contained" type="submit">Send</Button></p>
            </form>
        </div>
    )
};

export default CreateNews;