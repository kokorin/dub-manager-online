import React from "react";
import {Anime} from "./domain";

class AnimeTableRow extends React.Component<Anime, {}> {
    render() {
        return (<tr>
            <td>{this.props.id}</td>
            <td>{this.props.type}</td>
            <td>{this.props.titles}</td>
        </tr>);
    }
}

export default AnimeTableRow;
