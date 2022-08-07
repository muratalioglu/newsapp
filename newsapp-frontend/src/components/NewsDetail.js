import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import Navbar from "./Navbar";

const NewsDetail = () => {    

    const [news, setNews] = useState({"title": null, "content": null, "imageUrl": null});

    let { newsId } = useParams();

    useEffect(() => {
        
        fetch(`http://localhost:8080/news/${newsId}`)
            .then(res => res.json())
            .then(
                res => 
                setNews(
                    {
                        "title": res.title,
                        "content": res.content,
                        "imageUrl": res.imageUrl
                    }
                )
            )
    }, [newsId])

    return (
        <div>
            <Navbar />
            <h3>{news.title}</h3>
            <p>{news.content}</p>
            <img src={news.imageUrl} alt={news.title} />
            <p>
                <Link 
                    to={"/news/edit/" + newsId}
                    state={news}>
                    Edit
                </Link>
                <Link
                    to={"/news/edit"}
                >
                </Link>
            </p>
        </div>
    )
};

export default NewsDetail;