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
                <table>
                    <tbody>
                        <tr>
                            <td><label>Title</label></td>
                        </tr>
                        <tr>
                            <td><input type="text" name="title" value={news.title} onChange={handleTitleInputChange} /></td>
                        </tr>
                        <tr>
                            <td><label>Content</label></td>
                        </tr>
                        <tr>
                            <td><textarea rows="10" cols="50" name="content" value={news.content} onChange={handleContentInputChange} /></td>
                        </tr>
                        <tr>
                            <td><label>Görsel</label></td>
                        </tr>
                        <tr>
                            <td><input type="file" accept="image/*" onChange={handleImageInputChange} /></td>
                        </tr>
                        <tr>
                            <td><button type="submit">Publish</button></td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    )
};

export default CreateNews;