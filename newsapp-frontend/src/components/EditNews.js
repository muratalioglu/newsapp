import { Button, Stack, TextField } from "@mui/material";
import DeleteIcon from '@mui/icons-material/Delete';
import { useState } from "react";

import { useLocation, useParams } from "react-router-dom";
import Navbar from "./Navbar";

const EditNews = () => {

    const { newsId } = useParams();

    const location = useLocation();
    const [news, setNews] = useState(location.state);
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
        }
    };

    const handleImageInputRemove = () => {
        setSelectedImageFile(
            () => ({
                file: "",
                preview: ""
            })
        )
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
    }

    const sendNewsForm = async (e) => {

        const response = await uploadImageFile(e);

        const formData = new FormData();
        formData.append("title", news.title)
        formData.append("content", news.content)
        formData.append("imageUrl", response.headers.get("location"))

        fetch(`http://localhost:8080/news/${newsId}`,
            {
                method: "PATCH",
                body: formData
            }
        )

        //window.location.href = `/news/${newsId}`;
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
                    {/* <ImageConditional imageFile={selectedImageFile} imageUrl={news.imageUrl} /> */}
                    <img src={news.imageUrl} />
                <p>
                    <Button variant="contained" component="label">
                        Choose Image
                        <input hidden type="file" accept="image/*" onChange={handleImageInputChange} />
                    </Button>
                    {selectedImageFile.preview.length ? (<Button variant="outlined" startIcon={<DeleteIcon />} component="label" onClick={handleImageInputRemove}>Remove</Button>) : <div />}
                </p>
                <p><Button variant="contained" type="submit">Publish</Button></p>
            </form>
        </div>
    )

};

export default EditNews;