class Navbar extends HTMLElement {
    constructor() {
        super();
    }

    static get observedAttributes() {
        return ["cart-items-count"];
    }

    /* Invoked each time the custom element is appended into a document-connected element. */
    connectedCallback() {
        // TODO: Generate this automatically once localstorage is set up
        const isAuthenticated = localStorage.getItem("access-token") !== null;
        const initials = "V6";

        this.innerHTML = `
            <div class="relative flex flex-row h-20 bg-gradient-to-t from-[#1e2328] to-[#191e23] justify-between px-10 py-2 z-50">
                <!-- Wrapper for middle UT logo -->
                <div class="flex items-center h-full w-max">
                    <a href="/">
                        <img src="assets/images/utwenteLogo.png" />
                    </a>
                </div>
                <!-- Wrapper for right-hand side -->
                <div class="flex flex-row items-center gap-2">
                    ${isAuthenticated ? `
                        <!-- Dropdown navigation icon -->
                        <div class="dropdown dropdown-end">
                            <label tabindex="0" class="btn btn-ghost btn-circle">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7" /></svg>
                            </label>
                            <ul tabindex="0" class="menu menu-compact dropdown-content mt-5 p-2 shadow-md bg-[#1e2328] rounded-md w-52">
                                <li><a href="/">Polls</a></li>
                                <li><a href="./proposeProject.html">Propose Project</a></li>
                                <li><a href="./news.html">News</a></li>
                                <li><a href="./leaderboard.html">Leaderboard</a></li>
                                <li><a href="./shop.html">Store</a></li>
                                <div class="divider"></div>
                                <li class="-mt-2"><a onclick="APILogoutCall()">Log out</a></li>
                            </ul>
                        </div>
                        <!-- Profile Icon -->
                        <div class="avatar placeholder">
                            <div class="btn btn-ghost btn-circle bg-neutral-focus text-neutral-content w-12">
                                <a href="./profile.html">
                                    <span>${initials}</span>
                                </a>
                            </div>
                        </div> 
                        <!-- Cart icon -->
                        <label class="indicator" for="shopping-cart-drawer">
                            <svg class="w-10 h-10 text-blue-500 hover:text-blue-600 transition-all duration-200 cursor-pointer" fill="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                                <path d="M2.25 2.25a.75.75 0 000 1.5h1.386c.17 0 .318.114.362.278l2.558 9.592a3.752 3.752 0 00-2.806 3.63c0 .414.336.75.75.75h15.75a.75.75 0 000-1.5H5.378A2.25 2.25 0 017.5 15h11.218a.75.75 0 00.674-.421 60.358 60.358 0 002.96-7.228.75.75 0 00-.525-.965A60.864 60.864 0 005.68 4.509l-.232-.867A1.875 1.875 0 003.636 2.25H2.25zM3.75 20.25a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0zM16.5 20.25a1.5 1.5 0 113 0 1.5 1.5 0 01-3 0z"></path>
                            
                                <div class="absolute inline-flex items-center justify-center w-6 h-6 text-xs font-bold text-white bg-blue-500 border-2 border-white rounded-full -top-2 -right-2 dark:border-gray-900" id="cart-items-count-badge">${count}</div>
                            </svg>
                        </label>
                    ` : `
                        <button class="btn btn-ghost"><a href="/login.html">Log in</a></button>
                        <button class="btn btn-info bg-blue-400 hover:bg-blue-500 border-none"><a href="/signup.html">Sign up</a></button>
                    `}
                </div>
            </div>
        `;

        window.addEventListener("cart-change", () => {
            this.querySelector("#cart-items-count-badge").innerHTML = getCartCountAndTotal().count;
        })
    }
}

customElements.define("navbar", Navbar);