import React from "react";
import { render, screen } from "@testing-library/react";
import AnimeTableHead from "../AnimeTableHead";

describe("AnimeTableHead", () => {
    test("should render component", () => {
        render(
            <table>
                <thead>
                    <AnimeTableHead />
                </thead>
            </table>,
        );
        expect(screen.getByText("ID")).toBeDefined();
        expect(screen.getByText("TYPE")).toBeDefined();
        expect(screen.getByText("TITLE")).toBeDefined();
    });
});
