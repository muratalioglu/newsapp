import { Button, Stack, TextField } from "@mui/material";
import { useState } from "react";

import { Link, useLocation, useParams } from "react-router-dom";
import Navbar from "./Navbar";

const EditNews = () => {

    const { newsId } = useParams();

    const location = useLocation();
    const [news, setNews] = useState(location.state);    

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

    const handleImageUrlInputChange = (e) => {
        setNews(
            (news) => ({
                ...news,
                imageUrl: e.target.value
            })
        )
    };

    const sendNewsForm = (e) => {

        e.preventDefault();

        const formData = new FormData();
        formData.append("title", news.title)
        formData.append("content", news.content)
        formData.append("imageUrl", news.imageUrl)

        fetch(`http://localhost:8080/news/${newsId}`,
            {
                method: "PATCH",
                body: formData
            }
        )

        window.location.href = `/news/${newsId}`;
    };

    return (
        <div>
            <Navbar />
            <h3>Edit News</h3>
            <form onSubmit={sendNewsForm}>
                <Stack spacing={2}>
                    <TextField id="title" label="Title" variant="outlined" value={news.title} onChange={handleTitleInputChange} />
                    <TextField id="content" label="Content" multiline fullWidth rows={10} value={news.content} onChange={handleContentInputChange} />
                </Stack>
                <p><Button variant="contained" type="file" name="filename" onChange={handleImageUrlInputChange}>Choose Image</Button></p>
                <p><Button variant="contained">Publish</Button></p>
            </form>
        </div>
    )

};

export default EditNews;