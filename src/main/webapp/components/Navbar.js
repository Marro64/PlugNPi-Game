class Navbar extends HTMLElement {
    constructor() {
        super();
    }

    /* Invoked each time the custom element is appended into a document-connected element. */
    connectedCallback() {
        // TODO: Generate this automatically once localstorage is set up
        const isAuthenticated = false;
        const initials = "JD";

        this.innerHTML = `
            <div class="flex flex-row h-20 bg-gradient-to-t from-[#1e2328] to-[#191e23] justify-between px-10 py-2">
                <!-- Wrapper for middle UT logo -->
                <div class="flex items-center h-full w-max">
                    <a href="/">
                        <img src="/assets/images/plug'n'pi-logo.png"  width="200" height="200"/>
                    </a>
                </div>
                <!-- Wrapper for right-hand side -->
                <div class="flex flex-row items-center gap-2">
                    ${isAuthenticated ? `
                        <!-- Dropdown navigation icon -->
                        <div class="flex">
                            <div class="dropdown dropdown-end">
                                <label tabindex="0" class="btn btn-ghost btn-circle">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7" /></svg>
                                </label>
                                <ul tabindex="0" class="menu menu-compact dropdown-content mt-5 p-2 shadow-md bg-[#1e2328] rounded-md w-52">
                                    <li><a>Leaderboard</a></li>
                                    <li><a>Something Else</a></li>
                                    <li><a>Something Else 2</a></li>
                                </ul>
                            </div>
                        </div>
                        <!- Profile Icon -->
                        <div class="flex">
                            <div class="avatar placeholder">
                                <div class="btn btn-ghost btn-circle bg-neutral-focus text-neutral-content w-12">
                                    <span>${initials}</span>
                                </div>
                            </div> 
                        </div>
                    ` : `
                        <button class="btn btn-ghost"><a href="/WEB-INF/pages/home/login.html">Log in</a></button>
                        <button class="btn btn-info bg-blue-400 hover:bg-blue-500 border-none"><a href="/WEB-INF/pages/home/signup.html">Sign up</a></button>
                    `}
                </div>
            </div>
        `;
    }
}

customElements.define("navbar", Navbar);