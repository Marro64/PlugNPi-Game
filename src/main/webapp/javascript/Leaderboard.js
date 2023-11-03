class Leaderboard extends HTMLElement {
    constructor() {
        super();
    }
    /* Invoked each time the custom element is appended into a document-connected element. */
    connectedCallback() {
        /* Leaderboard data must be a json object */
        const leaderboardData = this.attributes.leaderboardData?.value;
        const leaderboardDataDecoded = JSON.parse(leaderboardData);

        /* Wrapper div for the whole table */
        const wrapperDiv = document.createElement("div");
        wrapperDiv.classList.add("rounded-md", "overflow-hidden");

        /* Table element */
        const table = document.createElement("table");
        table.classList.add("w-full", "text-sm", "text-left", "text-gray-500");

        /* Table head */
        const tableHead = document.createElement("thead");
        tableHead.classList.add("text-xs", "text-gray-700", "uppercase", "bg-gray-100");

        /* Table head row */
        const tableHeadRow = document.createElement("tr");

        const headerCellPosition = document.createElement("th");
        headerCellPosition.classList.add("w-12", "px-6", "py-3");
        headerCellPosition.innerHTML = "username";

        const headerCellPerson = document.createElement("th");
        headerCellPerson.classList.add("w-64", "px-6", "py-3");
        headerCellPerson.innerHTML = "distance";

        const headerCellEarned = document.createElement("th");
        headerCellEarned.classList.add("px-6", "py-3");
        headerCellEarned.innerHTML = "date_of_record";

        tableHeadRow.append(headerCellPosition, headerCellPerson, headerCellEarned);
        tableHead.appendChild(tableHeadRow);

        /* Table body */
        const tableBody = document.createElement("tbody");

        /* Going over all leaderboard data entries */
        leaderboardDataDecoded.forEach((data,index) => {
            const rank = index + 1;

            /* Creating table body row */
            const tr = document.createElement("tr");
            tr.classList.add("bg-white", "border-b");

            /* Creating table data cells */
            const bodyCellPosition = document.createElement("td");
            bodyCellPosition.classList.add("flex", "justify-between", "items-center", "px-6", "py-4");
            bodyCellPosition.innerHTML = rank;

            /* Add medal */
            if (rank <= 3) {
                bodyCellPosition.insertAdjacentHTML("beforeend", `
                    <svg xmlns="http://www.w3.org/2000/svg"
                        class="icon icon-tabler icon-tabler-jewish-star-filled ${rank === 1 ? `text-amber-300` : rank === 2 ? `text-gray-300` : `text-amber-700`} fill-current"
                        width="24" height="24" viewBox="0 0 24 24" stroke-width="1"
                        stroke="currentColor" stroke-linecap="round" stroke-linejoin="round">
                        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
                        <path
                        d="M8.433 6h-5.433l-.114 .006a1 1 0 0 0 -.743 1.508l2.69 4.486l-2.69 4.486l-.054 .1a1 1 0 0 0 .911 1.414h5.434l2.709 4.514l.074 .108a1 1 0 0 0 1.64 -.108l2.708 -4.514h5.435l.114 -.006a1 1 0 0 0 .743 -1.508l-2.691 -4.486l2.691 -4.486l.054 -.1a1 1 0 0 0 -.911 -1.414h-5.434l-2.709 -4.514a1 1 0 0 0 -1.714 0l-2.71 4.514z"
                        stroke-width="0" fill="currentColor"></path>
                    </svg>
                `);
            }

            const bodyCellPerson = document.createElement("td");
            bodyCellPerson.classList.add("px-6", "py-4");
            bodyCellPerson.innerHTML = data.username;

            const bodyCellPoints = document.createElement("td");
            bodyCellPoints.classList.add("px-6", "py-4");
            bodyCellPoints.innerHTML = data.distance;

            tr.append(bodyCellPosition, bodyCellPerson, bodyCellPoints);
            tableBody.appendChild(tr);
        });

        /* Finalizing */
        table.append(tableHead, tableBody);
        wrapperDiv.appendChild(table);

        this.appendChild(wrapperDiv);
    }
}

customElements.define("leaderboard-table", Leaderboard);
