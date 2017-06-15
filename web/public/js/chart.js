class Charts {

    /**
     * Constructor
     * @param {Objects} datas 
     */
    constructor(datas, id) {
        this.data = datas;
        this.id = id;
        this.ctx = document.getElementById('chart').getContext('2d')
    }

    /**
     * Filter Data
     * @void
     * @return {Object}
     */
    filterData() {
        let datasets = [];
        let labels = [];

        for (let idx in this.data) {
            datasets.push(this.data[idx].counter);
            labels.push(this.data[idx].date);
        }

        return {
            datas: datasets,
            labels: labels
        }
    }

    /**
     * Build Chart
     * @void
     */
    buildChart() {
        let {datas, labels} = this.filterData(this.data);

        if (datas.length == 0) {
            document.getElementById('hide').innerHTML = `
            <div class="alert alert-warning" role="alert">
                Nobody clicked on the link <strong>${this.id}</strong>
            </div>
            
            `;
            return Promise.reject('data is empty');
        }

        if (this.chart !== undefined || this.chart !== null)
            this.cleanChart();

        this.chart = new Chart(this.ctx, {
            type: 'bar',
            data: {
                datasets: [{
                    data: datas
                }],
                labels: labels
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero:true
                        }
                    }]
                }
            }
        });

        return Promise.resolve();
    }

    /**
     * Clean Chart
     */
    cleanChart() {
        try {
            document.getElementById('hide').innerHTML = ``;
            this.chart.destroy();
        } catch(e) {
            console.log('chart already destroyed');
        }
    }
}