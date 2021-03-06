
/**
 * Dashboard.js
 *          Control the front-end of the dashboard
 */
(() => {
    // Define a header
    const HEADERS = new Headers();
    // _ is a namespace where we reference our func
    const _ = Object.create({});

    // DOMReference - reference every DOM Object in the dashboard that we might use
    let DOMReference = Object.assign({}, {
        URLButton : {
            id: 'geturl',
            callback: () => {
                _.fetch({
                    req: 'api/url', 
                    type: 'POST', 
                    props: _.getDOMAttr('card', ['data-token'])
                })
                .then(res => _.buildTable(res))
                .catch(e => console.log(e));
            },
            exclude: false
        },
        BindLinkBut : {
            callback : (id) => {
                // build prop (yes i know this is badly done but it's late and i'm tired...)
                const token = _.getDOMAttr('card', ['data-token']);
                const obj = Object.create({});

                obj['data-token'] = token['data-token'];
                obj['id-url'] = id.toString();

                _.fetch({
                    req: 'api/stat',
                    type: 'POST',
                    props: obj
                })
                .then(res => {
                    let line = new Charts(res, id);
                    line.buildChart();
                })
                .catch(e => console.log(e));
            },
            exclude: true
        },
        BindUserUpdate: {
            id: 'submit-update',
            callback: (id) => {
                const token = _.getDOMAttr('card', ['data-token'])
                const data = Object.assign({}, {
                   username: document.getElementById('username').value,
                   oldpwd: document.getElementById('old-password').value,
                   password: document.getElementById('password').value,
                   mail: document.getElementById('mail').value,
                   'data-token': token['data-token']
                });

                _.fetch({
                    req: 'api/account',
                    type: 'POST',
                    props: data
                })
                .then(res => {
                    console.log(res);
                    if (res.error) {
                        swal(
                            'Oops!',
                            `We haven't been able to update your profile, please retry`,
                            'error'
                        )
                    } else {
                         window.location = 'logout?type=profile';
                    }
                })
                .catch(e => swal(
                    'Oops!',
                    `We haven't been able to update your profile, please retry`,
                    'error'
                ));
            },
            exclude: false
        }
    });

    /**
     * Fetch
     * @param {Object} param
     * @return {Promise} fetch
     */
    _.fetch = (param) => {
        // We get the param using the destructuring method..
        let {req, type, props} = param;
        // Define the params of the request
        let fetchParams = {
            method: type,
            headers: HEADERS
        };

        if (type === 'POST') {
            fetchParams.body = JSON.stringify(props)        
        }

        HEADERS.append('param', JSON.stringify(props));

        // Fetch the resource toward the Servlet-Mapping
        return fetch(req, fetchParams)
            .then(res => {
                return res.json();
            })
            .then(json => {
                return Promise.resolve(json);
            })
            .catch(e => Promise.reject(e));
    };

    /**
     * Get DOM Attr
     *      Get a param
     * @param {String} DOMElement
     * @param {Array} prop
     * @return {Object} attr
     */
    _.getDOMAttr = (DOMElement, prop) => {
        let e = document.getElementById(DOMElement);
        let attr = Object.create({});

        // Set the object
        prop.map(d => {
            attr[d] = e.getAttribute(d);
        });

        return attr;
    }

    /**
     * _.Build Table
     */
    _.buildTable = json => {
        if (json == null) {
            swal(
                'Oops!',
                `No url has been found for your profile`,
                'warning'
            );

            return;
        }
        // Construct a table
        DOMString = '';
        let idArr = [];

        json.map(d => {
            DOMString += `
                <tr>
                    <td class="mx">${d.original_link}</td>
                    <td>http://localhost:5000/tinwork/b/${d.short_link}</td>
                    <td>${d.create_date}</td>
                    <td id="link-${d.id}"><button type="button" class="btn btn-secondary">See more</button></td>
                </tr>
            `;

            idArr.push(d.id);
        });

        document.getElementById('body').insertAdjacentHTML('beforeend', DOMString);
        // add event listener
        
        idArr.map(id => {
            document.getElementById(`link-${id}`).addEventListener('click', DOMReference.BindLinkBut.callback.bind(null, id));
        });
    };

 
    /**
     * _.Boot
     *      Initialize every DOMElement that we need for the dashboard
     */
    _.boot = () => {
        document.addEventListener('DOMContentLoaded', () => {
            for (let prop in DOMReference) {
                if (DOMReference[prop].exclude)
                    continue;

                let el = document.getElementById(DOMReference[prop].id);
                el.addEventListener('click', DOMReference[prop].callback);
            }  
        });
    };

    // Execute right away the boot function
    _.boot();
})();