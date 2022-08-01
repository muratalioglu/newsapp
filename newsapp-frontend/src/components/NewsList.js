import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

const NewsList = () => {

    const[newsList, setNewsList] = useState([]);

    useEffect(() => {
        fetch(`http://localhost:8080/news`)
            .then(res => res.json())
            .then(res => setNewsList(res))
    }, [])

    return(
        <div>
            {newsList.map(news =>
                <Link style={{ display: "block", margin: "1rem 0" }} key={news.id} to={"/news/" + news.id}>
                    {news.title}
                </Link>)
            }
        </div>
    )
};

export default NewsList;