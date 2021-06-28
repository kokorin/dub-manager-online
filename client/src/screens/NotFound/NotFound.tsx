import React, { FC } from "react";
import { NavLink } from "react-router-dom";
import "./NotFound.css";

const NotFound: FC = () => {
    return (
        <section className="notFound">
            <div className="img">
                <img src="https://assets.codepen.io/5647096/backToTheHomepage.png" alt="Back to the Homepage" />
                <img src="https://assets.codepen.io/5647096/Delorean.png" alt="El Delorean, El Doc y Marti McFly" />
            </div>
            <div className="text">
                <h1>404</h1>
                <h2>PAGE NOT FOUND</h2>
                <h3>BACK TO HOME?</h3>
                <NavLink to="/" className="yes">
                    YES
                </NavLink>
                <a href="https://www.youtube.com/watch?v=G3AfIvJBcGo">NO</a>
            </div>
        </section>
    );
};

export default NotFound;
